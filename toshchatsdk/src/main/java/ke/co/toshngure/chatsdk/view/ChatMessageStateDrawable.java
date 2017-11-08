package ke.co.toshngure.chatsdk.view;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;

/**
 * Created by himanshusoni on 06/09/15.
 */

public abstract class ChatMessageStateDrawable extends ColorDrawable {

    private boolean mPressed;
    private boolean mActive;

    public ChatMessageStateDrawable(int color) {
        super(color);
    }

    @Override
    protected boolean onStateChange(int[] state) {

        boolean pressed = isPressed(state);
        if (mPressed != pressed) {
            mPressed = pressed;
            onIsPressed(mPressed);
        }

        boolean active = isActive(state);
        if (mActive != active){
            mActive = active;
            onIsActive(mActive);
        }

        return true;
    }

    protected abstract void onIsPressed(boolean isPressed);

    protected abstract void onIsActive(boolean isActive);

    @Override
    public boolean setState(@NonNull int[] stateSet) {
        return super.setState(stateSet);
    }

    @Override
    public boolean isStateful() {
        return true;
    }

    private boolean isPressed(int[] state) {
        boolean pressed = false;
        for (int i = 0, j = state != null ? state.length : 0; i < j; i++) {
            if (state[i] == android.R.attr.state_pressed) {
                pressed = true;
                break;
            }
        }
        return pressed;
    }

    private boolean isActive(int[] state) {
        boolean active = false;
        for (int i = 0, j = state != null ? state.length : 0; i < j; i++) {
            if (state[i] == android.R.attr.state_active) {
                active = true;
                break;
            }
        }
        return active;
    }
}
