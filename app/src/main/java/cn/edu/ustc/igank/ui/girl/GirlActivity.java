package cn.edu.ustc.igank.ui.girl;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.components.DraweeEventTracker;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.edu.ustc.igank.R;
import cn.edu.ustc.igank.support.ImageDownloadTask;
import me.relex.photodraweeview.PhotoDraweeView;

public class GirlActivity extends AppCompatActivity {

    TextView title;
    private PhotoDraweeView mPhotoDraweeView;
    public String url;
    String desc;
    String _id;

    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private boolean isFABVisable=false;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_girl);

        getData();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new ImageDownloadTask(url,view).start();
            }
        });

        mPhotoDraweeView = (PhotoDraweeView) findViewById(R.id.photo_drawee_view);

        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setUri(url);
        controller.setOldController(mPhotoDraweeView.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null || mPhotoDraweeView == null) {
                    return;
                }
                mPhotoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());

                //fab.setVisibility(View.VISIBLE);
            }

        });
        mPhotoDraweeView.setController(controller.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Animation anim = AnimationUtils.loadAnimation(fab.getContext(), R.anim.fab_in);
        anim.setDuration(500L);
        anim.setInterpolator(INTERPOLATOR);
        fab.startAnimation(anim);
    }

    private void getData() {
        Intent intent=getIntent();
        url=intent.getStringExtra("url");
        desc=intent.getStringExtra("desc");
        _id=intent.getStringExtra("_id");
    }
}
