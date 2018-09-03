package com.hw.photomovie.render;

import android.graphics.Rect;
import com.hw.photomovie.PhotoMovie;
import com.hw.photomovie.segment.MovieSegment;

/**
 * Created by huangwei on 2015/5/26.
 */
public abstract class MovieRenderer<T> {
    protected PhotoMovie<T> mPhotoMovie;
    protected volatile int mElapsedTime;
    protected Rect mViewportRect = new Rect();
    protected T mPainter;
    protected boolean mEnableDraw = true;

    public MovieRenderer() {
    }

    public abstract void drawFrame(int elapsedTime);

    public MovieRenderer<T> setPainter(T painter) {
        mPainter = painter;
        return this;
    }

    public void drawMovieFrame(int elapsedTime) {
        if (mPhotoMovie == null || !mEnableDraw) {
            return;
        }
        PhotoMovie.SegmentPicker<T> segmentPicker = mPhotoMovie.getSegmentPicker();
        MovieSegment<T> movieSegment = segmentPicker.pickCurrentSegment(elapsedTime);
        if (movieSegment != null) {
            float segmentRate = segmentPicker.getSegmentProgress(movieSegment, elapsedTime);
            if (movieSegment.showNextAsBackground()) {
                MovieSegment<T> nextSegment = segmentPicker.getNextSegment(elapsedTime);
                if (nextSegment != null && nextSegment != movieSegment) {
                    nextSegment.drawFrame(mPainter, 0);
                }
            }
            movieSegment.drawFrame(mPainter, segmentRate);
        }
    }

    ;

    public void setMovieViewport(int l, int t, int r, int b) {
        if (mPhotoMovie != null) {
            for (MovieSegment<T> segment : mPhotoMovie.getMovieSegments()) {
                segment.setViewport(l, t, r, b);
            }
        }
        mViewportRect.set(l, t, r, b);
    }

    public void setPhotoMovie(PhotoMovie photoMovie) {
        mPhotoMovie = photoMovie;
        if (mViewportRect.width() > 0 && mViewportRect.height() > 0) {
            setMovieViewport(mViewportRect.left, mViewportRect.top, mViewportRect.right, mViewportRect.bottom);
        }
    }

    public PhotoMovie getPhotoMovie(){
        return mPhotoMovie;
    }

    public void enableDraw(boolean enableDraw) {
        mEnableDraw = enableDraw;
    }
}