package edu.rosehulman.lix4.petlf;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import edu.rosehulman.lix4.petlf.models.Post;

/**
 * Created by phillee on 8/14/2017.
 */


public class MyFirebaseMessagingService extends Service {
    public FirebaseDatabase mDatabase;
    private Context mContext;

    public MyFirebaseMessagingService(Context context) {
        super();
        mContext = context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("service: ", "here");
        mDatabase = FirebaseDatabase.getInstance();

        initilizeNotificationListener();
    }

    private void initilizeNotificationListener() {
        mDatabase.getReference()
                .orderByKey().limitToLast(1)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot != null) {
                            Post post = dataSnapshot.getValue(Post.class);
                            showNotification(post);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void showNotification(Post post) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_pets_black_48dp)
                .setContentTitle("Someone made a new post!")
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentText(post.getTitle())
                .setAutoCancel(true);

        Intent resultIntent = new Intent(mContext, MainActivity.class);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

        /* Update firebase set notifcation with this key to 1 so it doesnt get pulled by our notification listener*/
//        flagNotificationAsSent(notification_key);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

