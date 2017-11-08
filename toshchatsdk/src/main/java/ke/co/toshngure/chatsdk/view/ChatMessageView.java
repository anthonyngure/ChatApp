package ke.co.toshngure.chatsdk.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ke.co.toshngure.chatsdk.R;
import ke.co.toshngure.chatsdk.model.Conversation;


/**
 * Chat Message view to create chatting window view
 */
public class ChatMessageView extends RelativeLayout {

    @DrawableRes
    private static final int ARROW_RESOURCE = R.drawable.cmv_arrow;
    private static final int CORNER_RADIUS = 5;
    private static final int CONTENT_PADDING = 5;
    private static final int RIGHT_ARROW_ROTATION = 0;
    private static final int LEFT_ARROW_ROTATION = 180;
    private TintedBitmapDrawable mNormalDrawable;
    private TintedBitmapDrawable mPressedDrawable;
    private LayoutParams mArrowParams;
    private LayoutParams mContainerParams;
    private ImageView mArrow;
    private RelativeLayout mContainer;
    private int mPressedBackgroundColor;
    private int mBackGroundColor;

    public ChatMessageView(Context context) {
        this(context, null);
    }

    public ChatMessageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatMessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {

        mPressedBackgroundColor = ContextCompat.getColor(getContext(), R.color.bg_selected_message);


        //Arrow
        mArrow = new ImageView(getContext());
        mArrow.setId(ViewUtil.generateViewId());
        super.addView(mArrow);

        //Container
        mContainer = new RelativeLayout(getContext());
        //mContainer.setPadding(CONTENT_PADDING, CONTENT_PADDING, CONTENT_PADDING, CONTENT_PADDING);
        mContainer.setId(ViewUtil.generateViewId());
        super.addView(mContainer);

        //Just to make sure the params are fine
        //pullRight(true);

        this.setClickable(true);
    }

    private void setArrowVisible(boolean arrowVisible){
        if (!arrowVisible){
            mArrow.setVisibility(INVISIBLE);
        } else {
            mArrow.setVisibility(VISIBLE);
        }

    }

    public void pullLeft(boolean arrowVisible, Conversation conversation) {

        mBackGroundColor = conversation.getPartnerMessageBackground();
        //mBackGroundColor = BaseUtils.generateColor(conversation);

        Bitmap source = BitmapFactory.decodeResource(this.getResources(), ARROW_RESOURCE);
        Bitmap rotateBitmap = rotateBitmap(source, LEFT_ARROW_ROTATION);

        mNormalDrawable = new TintedBitmapDrawable(getResources(), rotateBitmap, mBackGroundColor);
        mPressedDrawable = new TintedBitmapDrawable(getResources(), rotateBitmap, mPressedBackgroundColor);
        mArrow.setImageDrawable(mNormalDrawable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mArrow.setImageTintList(ColorStateList.valueOf(mBackGroundColor));
        }

        mArrowParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        mArrowParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mArrow.setLayoutParams(mArrowParams);

        mContainerParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        mContainerParams.addRule(RelativeLayout.RIGHT_OF, mArrow.getId());
        mContainer.setLayoutParams(mContainerParams);

        setArrowVisible(arrowVisible);

        updateColors();
    }

    public void pullRight(boolean arrowVisible, Conversation conversation) {

        mBackGroundColor = conversation.getMyMessageBackground();

        Bitmap source = BitmapFactory.decodeResource(this.getResources(), ARROW_RESOURCE);
        Bitmap rotateBitmap = rotateBitmap(source, RIGHT_ARROW_ROTATION);

        mNormalDrawable = new TintedBitmapDrawable(getResources(), rotateBitmap, mBackGroundColor);
        mPressedDrawable = new TintedBitmapDrawable(getResources(), rotateBitmap, mPressedBackgroundColor);
        mArrow.setImageDrawable(mNormalDrawable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mArrow.setImageTintList(ColorStateList.valueOf(mBackGroundColor));
        }

        mArrowParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        mArrowParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mArrow.setLayoutParams(mArrowParams);


        mContainerParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        mContainerParams.addRule(RelativeLayout.LEFT_OF, mArrow.getId());
        mContainer.setLayoutParams(mContainerParams);

        setArrowVisible(arrowVisible);

        updateColors();
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        if (child != mArrow && child != mContainer) {
            removeView(child);
            mContainer.addView(child);
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void updateColors() {
        ChatMessageDrawable roundRectDrawable = new ChatMessageDrawable(mBackGroundColor, CORNER_RADIUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mContainer.setBackground(roundRectDrawable);
        } else {
            mContainer.setBackgroundDrawable(roundRectDrawable);
        }

        mNormalDrawable.setTint(mBackGroundColor);
        mPressedDrawable.setTint(mPressedBackgroundColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mArrow.setImageTintList(ColorStateList.valueOf(mBackGroundColor));
        }

        Drawable stateDrawable = new ChatMessageStateDrawable(Color.TRANSPARENT) {
            @Override
            protected void onIsPressed(boolean isPressed) {
                ChatMessageDrawable conRlBackground = (ChatMessageDrawable) mContainer.getBackground();
                if (isPressed) {
                    conRlBackground.setColor(mPressedBackgroundColor);
                    mArrow.setImageDrawable(mPressedDrawable);
                } else {
                    conRlBackground.setColor(mBackGroundColor);
                    mArrow.setImageDrawable(mNormalDrawable);
                }
                mContainer.invalidate();
                mArrow.invalidate();
            }

            @Override
            protected void onIsActive(boolean isActive) {
                ChatMessageDrawable conRlBackground = (ChatMessageDrawable) mContainer.getBackground();
                if (isActive) {
                    conRlBackground.setColor(mPressedBackgroundColor);
                    mArrow.setImageDrawable(mPressedDrawable);
                } else {
                    conRlBackground.setColor(mBackGroundColor);
                    mArrow.setImageDrawable(mNormalDrawable);
                }
                mContainer.invalidate();
                mArrow.invalidate();
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(stateDrawable);
        } else {
            setBackgroundDrawable(stateDrawable);
        }

    }

    public Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
