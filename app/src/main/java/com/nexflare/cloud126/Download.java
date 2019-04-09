package com.nexflare.cloud126;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Download extends AppCompatActivity {
    private static final String TAG = "PATH";
    RecyclerView mRecyclerView;
    ArrayList<String> mArrayList;
    Adapter adapter;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        mRecyclerView= (RecyclerView) findViewById(R.id.downloadRecycle);
        mArrayList=new ArrayList<>();
        adapter=new Adapter();
        populateArray();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
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
    class Adapter extends RecyclerView.Adapter<VH>{

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflator=getLayoutInflater();
            View v=inflator.inflate(R.layout.mainpage,parent,false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(final VH holder, final int position) {
            String s=mArrayList.get(position);
            holder.tv.setText(s);
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    File file = new File(path + "/" + holder.tv.getText());
                    intent.setDataAndType(Uri.fromFile(file),"application/pdf");
//                  intent.setDataAndType(FileProvider.getUriForFile(insideFile.this, BuildConfig.APPLICATION_ID + ".provider", file), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            holder.cv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    File fdelete=new File(path+File.separator+holder.tv.getText());
                    if(fdelete.exists()){
                        if(fdelete.delete()){
                            Toast.makeText(Download.this, "File Deleted", Toast.LENGTH_SHORT).show();
                            mArrayList.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(Download.this, "File not deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mArrayList.size();
        }
    }

    private void populateArray() {
        path = Environment.getExternalStorageDirectory() + File.separator + "Cloud!26";
        Log.d(TAG, "populateArray:");
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            mArrayList.add(new String(files[i].getName()));
        }
    }

}
