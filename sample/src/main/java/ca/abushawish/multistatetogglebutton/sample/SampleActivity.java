package ca.abushawish.multistatetogglebutton.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.abushawish.multistatetogglebutton.MultiStateToggleButton;

public class SampleActivity extends AppCompatActivity implements MultiStateToggleButton.OnStateChangeListener{

    @BindView(R.id.mstb_sample_button_1)
    protected MultiStateToggleButton mMultiStateToggleButton1;

    @BindView(R.id.mstb_sample_button_2)
    protected MultiStateToggleButton mMultiStateToggleButton2;

    private List<Integer> mDrawableResourceIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        ButterKnife.bind(this);

        mDrawableResourceIDs = new ArrayList<>();
        mDrawableResourceIDs.add(R.drawable.ic_filter_1_black_24px);
        mDrawableResourceIDs.add(R.drawable.ic_filter_2_black_24px);
        mDrawableResourceIDs.add(R.drawable.ic_filter_3_black_24px);
        mDrawableResourceIDs.add(R.drawable.ic_filter_4_black_24px);
        mDrawableResourceIDs.add(R.drawable.ic_filter_5_black_24px);
        mDrawableResourceIDs.add(R.drawable.ic_filter_6_black_24px);
        mDrawableResourceIDs.add(R.drawable.ic_filter_7_black_24px);

        mMultiStateToggleButton1.setOnStateChangesListener(this);
        mMultiStateToggleButton2.setOnStateChangesListener(this);
        mMultiStateToggleButton2.setDrawableResourceList(mDrawableResourceIDs);
    }

    @Override
    public void onStateChanged(View view, int newState) {

        String newStateString = null;
        switch (view.getId()) {
            case R.id.mstb_sample_button_1:
                newStateString = getResources().getString(R.string.current_state, newState);
                break;
            case R.id.mstb_sample_button_2:
                newStateString = getResources().getString(R.string.current_state, newState);
                break;
        }

        if (newStateString != null) {
            Toast.makeText(this, newStateString, Toast.LENGTH_SHORT).show();
        }
    }
}
