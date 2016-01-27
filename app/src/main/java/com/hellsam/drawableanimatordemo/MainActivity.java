package com.hellsam.drawableanimatordemo;

import android.animation.ValueAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements ValueAnimator.AnimatorUpdateListener {
    private ImageView mIV;
    private AnimationDrawable mAnimationDrawable;
    private Button mLeftBtn;
    private Button mRightBtn;
    private Button mJumpBtn;
    private Handler mHandler = new Handler();

    //是否正在往左运动，是否正在往右运动，是否正在跳跃
    private boolean isRidingLeft = false, isRidingRight = false, isJumping = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIV = (ImageView) findViewById(R.id.iv_canvas);
        mLeftBtn = (Button) findViewById(R.id.btn_left);
        mRightBtn = (Button) findViewById(R.id.btn_right);
        mJumpBtn = (Button) findViewById(R.id.btn_jump);
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //先加载一下帧动画资源，避免第一次加载的时候的卡顿
                        mIV.setBackgroundResource(R.drawable.riding);
                        mAnimationDrawable = (AnimationDrawable) mIV.getBackground();
                        mIV.setBackgroundResource(R.drawable.jump);
                        mAnimationDrawable = (AnimationDrawable) mIV.getBackground();
                        mIV.setBackgroundResource(R.mipmap.riding_0);
                    }
                });
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!isJumping) {
//                            ride();
//                        }
//                        isRidingRight = true;
//                        isRidingLeft = false;
//                        mIV.animate().cancel();
//                        mIV.animate().translationXBy(10000).setInterpolator(new LinearInterpolator()).setDuration(20000).start();
//                        mIV.animate().setUpdateListener(MainActivity.this);
//                        mRightBtn.setBackgroundColor(Color.RED);
//                    }
//                }, 200);
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        jump();
//                        final Drawable temp = mJumpBtn.getBackground();
//                        mJumpBtn.setBackgroundColor(Color.RED);
//                        mHandler.postDelayed(new Runnable() {
//                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                            @Override
//                            public void run() {
//                                mJumpBtn.setBackground(temp);
//                            }
//                        }, 200);
//                    }
//                }, 3250);
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        jump();
//                        final Drawable temp = mJumpBtn.getBackground();
//                        mJumpBtn.setBackgroundColor(Color.RED);
//                        mHandler.postDelayed(new Runnable() {
//                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                            @Override
//                            public void run() {
//                                mJumpBtn.setBackground(temp);
//                            }
//                        }, 200);
//                    }
//                }, 5500);
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        jump();
//                        final Drawable temp = mJumpBtn.getBackground();
//                        mJumpBtn.setBackgroundColor(Color.RED);
//                        mHandler.postDelayed(new Runnable() {
//                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                            @Override
//                            public void run() {
//                                mJumpBtn.setBackground(temp);
//                            }
//                        }, 200);
//                    }
//                }, 6600);
            }
        });

        mJumpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump();
            }
        });
        mRightBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isJumping) {
                            ride();
                        }
                        isRidingRight = true;
                        isRidingLeft = false;
                        ride2Right();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!isRidingLeft) {
                            mIV.animate().cancel();
                        }
                        if (!isJumping && !isRidingLeft) {
                            mAnimationDrawable.stop();
                        }
                        isRidingRight = false;
                        break;
                }
                return false;
            }
        });
        mLeftBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isJumping) {
                            ride();
                        }
                        isRidingLeft = true;
                        isRidingRight = false;
                        ride2Left();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!isRidingRight) {
                            mIV.animate().cancel();
                        }
                        if (!isJumping && !isRidingRight) {
                            mAnimationDrawable.stop();
                        }
                        isRidingLeft = false;
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 判断车子是否在运动
     * @return
     */
    private boolean isRiding() {
        return isRidingLeft || isRidingRight;
    }

    /**
     * 播放骑行中（车轮滚动状态）的帧动画
     */
    private void ride() {
        mIV.setBackgroundResource(R.drawable.riding);
        mAnimationDrawable = (AnimationDrawable) mIV.getBackground();
        if (mAnimationDrawable != null && !mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
    }

    /**
     * 播放跳跃的帧动画
     */
    private void jump() {
        mIV.setBackgroundResource(R.drawable.jump);
        mAnimationDrawable = (AnimationDrawable) mIV.getBackground();
        if (mAnimationDrawable != null && !mAnimationDrawable.isRunning()) {
            mAnimationDrawable.stop();
            mAnimationDrawable.start();
            isJumping = true;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAnimationDrawable.stop();
                    isJumping = false;
                    if (isRiding()) {
                        ride();
                    }
                }
            }, 21 * 50);
        }
    }

    /**
     * 往右运动
     */
    private void ride2Right() {
        mIV.animate().cancel();
        mIV.animate().translationXBy(10000).setInterpolator(new LinearInterpolator()).setDuration(20000).start();
        mIV.animate().setUpdateListener(MainActivity.this);
    }

    /**
     * 往左运动
     */
    private void ride2Left() {
        mIV.animate().cancel();
        mIV.animate().translationXBy(-10000).setInterpolator(new LinearInterpolator()).setDuration(20000).start();
        mIV.animate().setUpdateListener(MainActivity.this);
    }

    /**
     * 当自行车出了屏幕边界后，重新从另一面驶入
     * @param animation
     */
    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (mIV.getTranslationX() > getWindow().getDecorView().getWidth() - 34) {
            mIV.setTranslationX(-mIV.getWidth() + 70);
            ride2Right();
        } else if (mIV.getTranslationX() < -mIV.getWidth() + 70) {
            mIV.setTranslationX(getWindow().getDecorView().getWidth() - 34);
            ride2Left();
        }

    }

}
