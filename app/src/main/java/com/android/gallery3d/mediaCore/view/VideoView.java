package com.android.gallery3d.mediaCore.view;

import android.content.Context;
import android.graphics.Rect;

import com.android.gallery3d.glrenderer.GLCanvas;
import com.android.gallery3d.mediaCore.Utils.Annotation;
import com.android.gallery3d.mediaCore.Utils.Utils;
import com.android.gallery3d.mediaCore.Utils.VideoScreenNail;
import com.android.gallery3d.mediaCore.anim.MediaStream;
import com.android.gallery3d.mediaCore.view.Inte.OnNotifyChangeListener;
import com.android.gallery3d.mediaCore.view.Inte.StateIs;
import com.android.gallery3d.mediaCore.view.Inte.VIPlayControl;
import com.android.gallery3d.ui.GLView;

/**
 * Created by linusyang on 16-12-1.
 */

public class VideoView extends GLView implements VIPlayControl, OnNotifyChangeListener, StateIs {

    public interface PlayStateListener {
        void onCompletion();
    }

    private Context mContext;
    private Rect mWindowRect;
    private PlayBits mPlayBits;
    private PlayStateListener mPlayStateListener;

    private VideoScreenNail videoScreenNail;
    private boolean first;

    public void setVideoScreenNail(VideoScreenNail videoScreenNail) {
        this.videoScreenNail = videoScreenNail;
    }

    public VideoScreenNail getVideoScreenNail() {
        return videoScreenNail;
    }

    public VideoView(Context context) {
        mContext = context;
        mPlayBits = new PlayBits();
        mPlayBits.setOnNotifyChangeListener(this);
        setBackgroundColor(new float[]{0,0,0,0});
    }

    @Override
    protected void onLayout(boolean changeSize, int left, int top, int right, int bottom) {
        super.onLayout(changeSize, left, top, right, bottom);
        mWindowRect = new Rect(left, top, right, bottom);
        int width = right - left;
        int height = bottom - top;
        mPlayBits.setResolution(width, height);
    }

    public void setPlayStateListener(PlayStateListener playStateListener) {
        mPlayStateListener = playStateListener;
    }

    @Override
    protected void render(GLCanvas canvas) {
        super.render(canvas);
        if(videoScreenNail!=null&&!first){
            videoScreenNail.draw(canvas,0,0,0,0);
            first = true;
        }
        mPlayBits.onDraw(canvas);
    }

    public void prepare(MediaStream mediaStream) {
        Utils.checkNull(mediaStream, "VideoVIew prepare NULL");
        mPlayBits.prepare(mediaStream);
    }

    public void prepareNext(MediaStream mediaStream){
        Utils.checkNull(mediaStream, "VideoVIew prepare NULL");
        mPlayBits.prepareNext(mediaStream);
    }

    @Override
    @Annotation.IInterface("VIPlayControl")
    public void prepare() {
        mPlayBits.prepare();
    }

    @Override
    @Annotation.IInterface("VIPlayControl")
    public void start() {
        mPlayBits.start();
    }

    @Override
    @Annotation.IInterface("VIPlayControl")
    public void restart() {
        mPlayBits.restart();
    }

    @Override
    @Annotation.IInterface("VIPlayControl")
    public void pause() {
        mPlayBits.pause();
    }

    @Override
    @Annotation.IInterface("VIPlayControl")
    public void stop() {
        mPlayBits.stop();
    }

    @Override
    @Annotation.IInterface("VIPlayControl")
    public void seekTo(long durationT) {
        mPlayBits.seekTo(durationT);
    }

    @Override
    @Annotation.IInterface("VIPlayControl")
    public int getPlayState() {
        return mPlayBits.getPlayState();
    }

    @Override
    @Annotation.IInterface("VIPlayControl")
    public long getProgress() {
        return mPlayBits.getProgress();
    }

    @Override
    public void setDuration(long duration) {
        mPlayBits.setDuration(duration);
    }

    @Override
    @Annotation.IInterface("VIPlayControl")
    public long getDuration() {
        return mPlayBits.getDuration();
    }

    @Override
    @Annotation.IInterface("OnNotifyChangeListener")
    public void doInvalidate() {
        invalidate();
    }

    @Override
    public void notifyCompletion() {
        if(mPlayStateListener != null)  mPlayStateListener.onCompletion();
    }


}
