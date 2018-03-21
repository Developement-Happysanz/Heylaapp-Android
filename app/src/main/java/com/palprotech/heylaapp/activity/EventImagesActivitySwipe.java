package com.palprotech.heylaapp.activity;

import android.animation.Animator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.palprotech.heylaapp.R;
import com.palprotech.heylaapp.app.AppController;
import com.palprotech.heylaapp.bean.support.Event;
import com.palprotech.heylaapp.bean.support.EventPictureList;
import com.palprotech.heylaapp.helper.AViewFlipper;
import com.palprotech.heylaapp.helper.AlertDialogHelper;
import com.palprotech.heylaapp.helper.ProgressDialogHelper;
import com.palprotech.heylaapp.interfaces.DialogClickListener;
import com.palprotech.heylaapp.servicehelpers.ServiceHelper;
import com.palprotech.heylaapp.serviceinterfaces.IServiceListener;
import com.palprotech.heylaapp.utils.HeylaAppConstants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Narendar on 20/03/18.
 */

public class EventImagesActivitySwipe extends AppCompatActivity implements View.OnClickListener, IServiceListener, DialogClickListener, AdapterView.OnItemClickListener {
    protected ProgressDialogHelper progressDialogHelper;
    private ServiceHelper serviceHelper;
    private Animator mCurrentAnimator;
    ImageLoader uImageLoader = AppController.getInstance().getUniversalImageLoader();
    private Event event;
    private GestureDetector mGestureDetector;
    private static final String TAG = EventImagesActivitySwipe.class.getName();
    private ArrayList<String> imgList = new ArrayList<>();
    private int mShortAnimationDuration = 500;
    AViewFlipper aViewFlipper;
    int pageNumber = 0, totalCount = 0;
    protected boolean isLoadingForFirstTime = true;
    EventPictureList eventPictureList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_list);
        event = (Event) getIntent().getSerializableExtra("eventObj");
        serviceHelper = new ServiceHelper(this);
        serviceHelper.setServiceListener(this);
        progressDialogHelper = new ProgressDialogHelper(this);

        aViewFlipper=  findViewById(R.id.banner_new);
        loadEventImages();


        Log.d(TAG, "Image uri is" + event.getEventBanner());
    }

    private void loadEventImages() {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(HeylaAppConstants.KEY_EVENT_ID, event.getId());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        progressDialogHelper.showProgressDialog(getString(R.string.progress_loading));
        String url = HeylaAppConstants.BASE_URL + HeylaAppConstants.EVENT_IMAGES;
        serviceHelper.makeGetServiceCall(jsonObject.toString(), url);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

    GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {

            float sensitvity = 300;
            if ((e1.getX() - e2.getX()) > sensitvity) {
                SwipeLeft();
            } else if ((e2.getX() - e1.getX()) > sensitvity) {
                SwipeRight();
            }

            return true;
        }

    };

    GestureDetector gestureDetector = new GestureDetector(simpleOnGestureListener);


    private void SwipeLeft() {
        aViewFlipper.setInAnimation(this, R.anim.left_in);
        aViewFlipper.showNext();

    }


    private void SwipeRight() {
        aViewFlipper.setInAnimation(this, R.anim.left_out);
        aViewFlipper.showPrevious();

    }


//    public class ImageAdapter extends BaseAdapter {
//        private Context context;
//        private int itemBackground;
//
//        public ImageAdapter(Context c) {
//            context = c;
//            // sets a grey background; wraps around the images
//          /*  TypedArray a =obtainStyledAttributes(R.styleable.MyGallery);
//            itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
//            a.recycle();*/
//        }
//
//        // returns the number of images
//        public int getCount() {
//            return imgList.size();
//        }
//
//        // returns the ID of an item
//        public Object getItem(int position) {
//            return position;
//        }
//
//        // returns the ID of an item
//        public long getItemId(int position) {
//            return position;
//        }
//
//        // returns an ImageView view
//        public View getView(int position, View convertView, ViewGroup parent) {
//          /*  ImageView imageView = new ImageView(context);
//           // imageView.setImageResource(imgList[position]);
//            imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
//            imageView.setBackgroundResource(R.color.bg_gray);
//            imageView.setPadding(5,2,5,2);*/
//            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            convertView = inflater.inflate(R.layout.image_banner, null);
//            ImageView imageView = (ImageView) convertView.findViewById(R.id.banner);
//            String thumbnailUrl = getThumbnailImageUrl(imgList.get(position), 0, 0);
//            uImageLoader.displayImage(thumbnailUrl, imageView,
//                    new DisplayImageOptions.Builder()
//                            .showImageOnLoading(android.R.color.darker_gray)
//                            .cacheInMemory(true).cacheOnDisk(true).build(), loadingListener);
//            imageView.setAdjustViewBounds(true);
//
//            return convertView;
//        }
//    }

    private ImageLoadingListener loadingListener = new SimpleImageLoadingListener() {
        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            view.setEnabled(true);//only loadedImage is available we can click item
        }
    };

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onAlertPositiveClicked(int tag) {

    }

    @Override
    public void onAlertNegativeClicked(int tag) {

    }

    private boolean validateSignInResponse(JSONObject response) {
        boolean signInSuccess = false;
        if ((response != null)) {
            try {
                String status = response.getString("status");
                String msg = response.getString(HeylaAppConstants.PARAM_MESSAGE);
                Log.d(TAG, "status val" + status + "msg" + msg);

                if ((status != null)) {
                    if (((status.equalsIgnoreCase("activationError")) || (status.equalsIgnoreCase("alreadyRegistered")) ||
                            (status.equalsIgnoreCase("notRegistered")) || (status.equalsIgnoreCase("error")))) {
                        signInSuccess = false;
                        Log.d(TAG, "Show error dialog");
                        AlertDialogHelper.showSimpleAlertDialog(this, msg);

                    } else {
                        signInSuccess = true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return signInSuccess;
    }

    @Override
    public void onResponse(JSONObject response) {
        progressDialogHelper.hideProgressDialog();
        if (validateSignInResponse(response)) {
            try {

                final JSONArray getData = response.getJSONArray("Eventgallery");
                for(int i = 0; i < getData.length(); i++){
                    imgList.add(i,getData.getJSONObject(i).getString("event_banner"));
                    setImageInFlipr(imgList.get(i));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String error) {
        progressDialogHelper.hideProgressDialog();
        AlertDialogHelper.showSimpleAlertDialog(this, error);
    }

    private void setImageInFlipr(String imgUrl) {
        TouchImageView imageview = new TouchImageView(EventImagesActivitySwipe.this);
        Picasso.with(this).load(imgUrl).into(imageview);
        aViewFlipper.addView(imageview);
    }

}