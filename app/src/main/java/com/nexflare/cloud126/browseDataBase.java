package com.nexflare.cloud126;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class browseDataBase extends AppCompatActivity {
    private static final String FRAGMENT_TAG ="fragment" ;
    private static final String TAG = "TASK";
    /* FragmentManager mFragmentManager;
     FragmentTransaction mFragmentTransaction;*/
    ProgressBar progress;
    ArrayList<String> arrList;
    RecyclerView browseRecycle;
    String ref;
    Handler mHandler=new Handler();
    browseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsedatabase);
        progress= (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.VISIBLE);
        arrList=new ArrayList<>();
        Intent intent=getIntent();
        ref=intent.getStringExtra("String");
        getSupportActionBar().setTitle(ref);
        populateArray(ref);
        browseRecycle= (RecyclerView) findViewById(R.id.rvBrowse);
        //mFragmentManager=getSupportFragmentManager();
        adapter=new browseAdapter();
        browseRecycle.setLayoutManager(new LinearLayoutManager(this));
        browseRecycle.setAdapter(adapter);
        //setFragment(intent.getStringExtra("String"));
    }
    class VH extends RecyclerView.ViewHolder{

        CardView cv;
        TextView tv;
        public VH(View itemView) {
            super(itemView);
            cv= (CardView) itemView.findViewById(R.id.cardView);
            tv= (TextView) itemView.findViewById(R.id.tv);
        }
    }
    class browseAdapter extends RecyclerView.Adapter<VH>{

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=getLayoutInflater();
            View rootView=inflater.inflate(R.layout.mainpage,parent,false);
            //Toast.makeText(getActivity(), "HELLO World", Toast.LENGTH_SHORT).show();
            return new VH(rootView);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            final String s=arrList.get(position);
            //Toast.makeText(getActivity(), " IN "+ s, Toast.LENGTH_SHORT).show();
            holder.tv.setText(s);


            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String newref=ref+"/"+s;
                    DatabaseReference mDataBase=FirebaseDatabase.getInstance().getReference(newref);
                    mDataBase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChildren()){
                                Intent intent=new Intent(browseDataBase.this,browseDataBase.class);
                                intent.putExtra("String",newref);
                                startActivity(intent);
                            }
                            else{
                                createFolder();
                                FirebaseStorage storage=FirebaseStorage.getInstance();
                                final StorageReference store=storage.getReferenceFromUrl(dataSnapshot.getValue(String.class));
                                Toast.makeText(browseDataBase.this, dataSnapshot.getValue(String.class), Toast.LENGTH_SHORT).show();
                                String link=dataSnapshot.getValue(String.class);
                                downloadFile(link,store);
                                //new downloadFileFromUrl().execute(link,store.getParent().getName()+" "+store.getName());https://firebasestorage.googleapis.com/v0/b/cloud126-48041.appspot.com/o/Notes%2FSem%204%2FOS%2FOS%20T1.pdf?alt=media&token=fa557c48-4122-4a45-9e51-8a728bd7d305
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrList.size();
        }
    }

    private class downloadFileFromUrl extends AsyncTask<String, String, String>{
        NotificationCompat.Builder mBuilder;
        NotificationManager mManager;

        @Override
        protected String doInBackground(String... params) {
            int count;
            try {
                Log.d(TAG, "doInBackground: The Link of Download File "+params[0]);
                URL url=new URL(params[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                int lengthFile=connection.getContentLength();
                InputStream input=new BufferedInputStream(url.openStream());
                String filedr=Environment.getExternalStorageDirectory()+File.separator+"Cloud!26";
                File file=new File(filedr,params[1]);
                Log.d(TAG, "doInBackground: "+params[1]);
                OutputStream output=new FileOutputStream(file,true);
                byte data[]=new byte[1024];
                int total=0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lengthFile),params[1]);
                    // writing data to file
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();
                Log.e(TAG, "doInBackground: "+"transfer done");

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(browseDataBase.this,"transfer done",Toast.LENGTH_LONG).show();
                    }
                });

            } catch (final Exception e) {
                Log.e(TAG, "doInBackground: "+e.getMessage());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(browseDataBase.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBuilder = new NotificationCompat.Builder(browseDataBase.this);
            mManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Downloading ");

        }

        @Override
        protected void onPostExecute(String s) {
            mBuilder.setContentTitle("Downloaded");
            mBuilder.setProgress(0,0,false);
            mManager.notify(126,mBuilder.build());
            Log.d(TAG, "onPostExecute: "+"HERE I COME");
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            mBuilder.setContentText(values[1]);
            mBuilder.setProgress(100,Integer.valueOf(values[0]),false);
            mManager.notify(126,mBuilder.build());
            super.onProgressUpdate(values);
        }
    }

    private void downloadFile(String s, StorageReference store) {
        String url = s;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setDescription(store.getName());
        request.setTitle(store.getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
//        request.setDestinationInExternalPublicDir(String.valueOf(Environment.getExternalStorageDirectory()+"/Download"),store.getName());
        request.setDestinationUri(Uri.fromFile(new File(Environment.getExternalStorageDirectory()+"/Cloud!26/"+store.getName())));
// get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    private void createFolder() {
        File folder=new File(Environment.getExternalStorageDirectory()+File.separator+"Cloud!26");
        boolean s=true;
        if(!folder.exists()){
           s= folder.mkdirs();
        }
        if(!s){
            Toast.makeText(this, "Folder Not Created", Toast.LENGTH_SHORT).show();
        }
    }

    /*private void setFragment(String s) {
        listFragment ls=new listFragment();
        Bundle arguments = new Bundle();
        arguments.putString("String", s);
        ls.setArguments(arguments);
        mFragmentTransaction=mFragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.container,ls,FRAGMENT_TAG);
        mFragmentTransaction.commit();
    }*/
    private void populateArray(String reference) {
        DatabaseReference mDataBase= FirebaseDatabase.getInstance().getReference(reference);
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progress.setVisibility(View.INVISIBLE);
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Toast.makeText(browseDataBase.this, ds.getKey(), Toast.LENGTH_SHORT).show();
                    arrList.add(ds.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(browseDataBase.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
