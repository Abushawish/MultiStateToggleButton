package ca.abushawish.multistatetogglebutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Android's regular {@link android.widget.ToggleButton} offers only two states for the toggle (checked and unchecked),
 * MultiStateToggleButton fixes that by offering as many states as needed depending on the number of
 * drawable resource IDs in the list passed into this class, using {@link MultiStateToggleButton#setDrawableResourceList(List)}
 * or assigning an XML array into the drawable_resource_list attribute in the XML.
 *
 * @author mabushawish
 * @version 1.0.0
 */
public class MultiStateToggleButton extends AppCompatImageButton implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    private final int INVALID_RES_DEFAULT = -1;
    private final int STARTING_STATE_DEFAULT = 0;

    private List<Integer> mDrawableResourceList;
    private OnClickListener mWrappedOnClickListener;
    private OnStateChangeListener mOnStateChangeListener;
    private int mCurrentState;

    public MultiStateToggleButton(Context context) {
        super(context);
        if (!isInEditMode()) {
            init(context, null);
        }
    }

    public MultiStateToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    public MultiStateToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    /**
     * Initializes the class and extracts XML attributes
     *
     * @param context The calling Android context
     * @param attrs   The collection of XML attributes
     */
    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        mDrawableResourceList = new ArrayList<>();
        mCurrentState = STARTING_STATE_DEFAULT;

        super.setOnClickListener(this);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.MultiStateToggleButton, 0, 0);

        try {
            int arrayResourcesId = typedArray.getResourceId(R.styleable.MultiStateToggleButton_drawable_resource_list, INVALID_RES_DEFAULT);

            if (arrayResourcesId != INVALID_RES_DEFAULT) {
                final TypedArray resourcesArray = getResources().obtainTypedArray(arrayResourcesId);

                for (int i = 0; i < resourcesArray.length(); i++) {
                    final int resourceId = resourcesArray.getResourceId(i, INVALID_RES_DEFAULT);
                    if (resourceId != INVALID_RES_DEFAULT) {
                        mDrawableResourceList.add(resourceId);
                    } else {
                        throw new IllegalArgumentException(TAG + ": Invalid drawable resource ID found in the " +
                                "given mDrawableResourceList (drawable_resource_list in XML)");
                    }
                }

                if (resourcesArray.length() < 1) {
                    Log.w(TAG, "Empty mDrawableResourceList (drawable_resource_list in XML), is this intentional?");
                } else {
                    // Set default state to first drawable resource if array isn't empty
                    stateChangedAction(STARTING_STATE_DEFAULT);
                }

                resourcesArray.recycle();
            } else {
                Log.w(TAG, "No given/valid mDrawableResourceList (drawable_resource_list in XML), is this intentional?");
            }

        } finally {
            typedArray.recycle();
        }
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        /* Need mWrappedOnClickListener because otherwise the current onClickListener will get overwritten
           when setOnClickListener is called on an instance of this class. */
        mWrappedOnClickListener = onClickListener;
    }

    @Override
    public void onClick(View view) {
        if (mWrappedOnClickListener != null) {
            mWrappedOnClickListener.onClick(view);
        }
        nextState();
    }

    /**
     * Assigns a listener to trigger for when the current state changes
     *
     * @param onStateChangeListener The callback to be invoked when the state is changed
     */
    public void setOnStateChangesListener(@NonNull OnStateChangeListener onStateChangeListener) {
        mOnStateChangeListener = onStateChangeListener;
    }

    /**
     * Gets the current state
     *
     * @return The current state (> 0 and < size of drawableResourceList - 1)
     */
    public int getCurrentState() {
        return mCurrentState;
    }

    /**
     * Manually set the current state.
     *
     * @param stateToSetFor The state to set for (> 0 and < size of drawableResourceList - 1).
     */
    public void setState(@IntRange(from = 0) int stateToSetFor) {
        if (stateToSetFor < 0 || stateToSetFor > mDrawableResourceList.size() - 1) {
            throw new IllegalArgumentException(TAG + ": The given state to set for (" + stateToSetFor + ") is either less than zero or greater than " +
                    "the size of the given mDrawableResourceList (drawable_resource_list in XML).");
        }

        mCurrentState = stateToSetFor;

        stateChangedAction(stateToSetFor);
    }

    /**
     * Programmatically assign the drawableResourceList to set from when the state changes
     *
     * @param drawableResourceList A list of drawable resource IDs (R.drawable.pic_name_here) - Non-null, min size of 1.
     */
    public void setDrawableResourceList(@NonNull @Size(min = 1) List<Integer> drawableResourceList) {
        mDrawableResourceList = drawableResourceList;
        stateChangedAction(STARTING_STATE_DEFAULT);
    }


    /**
     * Increments the current state, wrapping to 0 if greater than mDrawableResourceList
     */
    private void nextState() {
        mCurrentState++;

        if (mCurrentState > mDrawableResourceList.size() - 1) {
            mCurrentState = STARTING_STATE_DEFAULT;
        }

        stateChangedAction(mCurrentState);
    }

    /**
     * A private function that performs the various actions that occur following a state change (informing
     * listeners, setting current viewable image resource, and some error checking.
     *
     * @param stateToSetFor The current state being assigned
     */
    private void stateChangedAction(@IntRange(from = 0) int stateToSetFor) {
        if (stateToSetFor >= 0 && stateToSetFor < mDrawableResourceList.size()) {
            setDrawableResource(stateToSetFor);
            if (mOnStateChangeListener != null) {
                mOnStateChangeListener.onStateChanged(this, stateToSetFor);
            }
        }
    }

    /**
     * Sets the current visible image resource from the mDrawableResourceList based on the given stateToSetFor
     *
     * @param stateToSetFor The state to assign the current image for
     */
    private void setDrawableResource(int stateToSetFor) {
        setImageResource(mDrawableResourceList.get(stateToSetFor));
        invalidate();
        requestLayout();
    }

    /**
     * Interface definition for a callback to be invoked when the state is changed.
     */
    public interface OnStateChangeListener {

        /**
         * Called when the current state has changed
         *
         * @param view     The view who's state just changed
         * @param newState The current state produced following the click.
         */
        void onStateChanged(View view, int newState);
    }
}