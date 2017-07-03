MultiStateToggleButton
===============

Android's regular ToggleButton offers only two states for the toggle (checked and unchecked), MultiStateToggleButton fixes that by offering as many states as needed depending on the number of drawable resource IDs passed in.

![](https://github.com/Abushawish/MultiStateToggleButton/blob/master/mstb-record.gif?raw=true){:height="50%" width="50%"}

Including in your project
-------------------------

```groovy
compile 'ca.abushawish.multistatetogglebutton:multistatetogglebutton:1.0'
```

Check here for the latest [Releases](https://github.com/Abushawish/MultiStateToggleButton/releases).

Usage
-----

Create an integer-array of drawable resource IDs in your [arrays.xml](https://developer.android.com/samples/MediaRouter/res/values/arrays.html)

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <integer-array name="flash_images_array">
        <item>@drawable/ic_flash_auto_black_24px</item>
        <item>@drawable/ic_flash_on_black_24px</item>
        <item>@drawable/ic_flash_off_black_24px</item>
    </integer-array>
</resources>
```

Then, in your layout file, add the 'MultiStateToggleButton' with the 'app:drawable_resource_list' attribute and the reference to the above created integer-array as it's value.

```xml
<ca.abushawish.multistatetogglebutton.MultiStateToggleButton
            android:id="@+id/mstb_sample_button"
            android:layout_width="@dimen/mstib_size_30"
            android:layout_height="@dimen/mstib_size_30"
            android:background="@android:color/transparent"
            app:drawable_resource_list="@array/flash_images_array"/>
```

**Alternativaly, programatically:**

Initialize the ```MultiStateToggleButton``` and populate a ```List<Integer>``` of your drawable resource IDs.

```java
private MultiStateToggleButton mMultiStateToggleButton = (MultiStateToggleButton) findViewById(R.id.mstb_sample_button);

private List<Integer> mDrawableResourceIDs; = new ArrayList<>();
mDrawableResourceIDs.add(R.drawable.ic_filter_1_black_24px);
mDrawableResourceIDs.add(R.drawable.ic_filter_2_black_24px);
...
mDrawableResourceIDs.add(R.drawable.ic_filter_7_black_24px);
```

Then, set the populated drawable resource ID list to the multiStateToggleButton object as so:

```java
mMultiStateToggleButton.setDrawableResourceList(mDrawableResourceIDs);
```

Implement the `MultiStateToggleButton.OnStateChangeListener`.

```java
public class SampleActivity extends Activity implements MultiStateToggleButton.OnStateChangeListener{

    ...

    @Override
    public void onStateChanged(View view, int newState) {
        switch (view.getId()) {
            case R.id.mstb_sample_button:
                // Do something on state changed or with the int newState
                break;
        }
    }
}
```

Developed By
--------------------
Moe Abushawish

License
-----------

```
Copyright 2015 Mohammed Abushawish

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[gif]: 
