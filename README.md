# 🖌️BrushEffcet

#### This is a simple animation effect for any android view.

##### It's very very simple and easy to use,so I don't know how to write the README.md for this project. 😅


## Preview
![Preview](/preview/preview.gif)

------
## Usage
```xml
<com.github.tommytc.lib.brusheffect.BrushEffectLayout
        android:id="@+id/layoutTitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:bel_duration="400"
        app:bel_orientation="horizontal"
        app:bel_reverse="true"
        app:bel_startColor="@color/colorAccent"
        app:bel_endColor="@color/colorAccent"
        app:bel_strokeWidth="1.0"
        app:bel_strokeCap="square">

        <Your view/>

    </com.github.tommytc.lib.brusheffect.BrushEffectLayout>
```
```java
//Set interpolators
brushEffectLayout.setInInterpolator();
brushEffectLayout.setOutInterpolator();

//Set a listener
brushEffectLayout.setListener(new BrushEffectLayout.Listener() {
            public void onStart() {
            }

            public void onCover() {
            }

            public void onFinish() {
            }
        });

//Start
brushEffectLayout.brush();
//End
brushEffectLayout.hide();

```
### That's all 😗 Hope it can help you.

