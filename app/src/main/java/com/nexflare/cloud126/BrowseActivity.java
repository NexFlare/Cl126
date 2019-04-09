package com.nexflare.cloud126;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.nexflare.cloud126.pojos.mainPagePojo;

import java.io.File;
import java.util.ArrayList;

public class BrowseActivity extends AppCompatActivity {
    private static final String TAG = "KEY WALA";
    DatabaseReference mDatabaseReference;
    RecyclerView rv;
    ArrayList<mainPagePojo> arrList;
    TextView tvNotes,tvDownload,tvMenu,tvTut;
    CardView cvNotes,cvDownload,cvMenu,cvTut;
    FirebaseAuth firebaseAuth;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_sign_out){
            if(firebaseAuth.getCurrentUser()!=null) {
                firebaseAuth.signOut();
                startActivity(new Intent(BrowseActivity.this,MainActivity.class));
                finish();
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        cvNotes= (CardView) findViewById(R.id.cardView1);
        cvTut= (CardView) findViewById(R.id.cardView2);
        cvMenu= (CardView) findViewById(R.id.cardView3);
        cvDownload= (CardView) findViewById(R.id.cardView4);
        firebaseAuth=FirebaseAuth.getInstance();
        cvNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    Intent intent=new Intent(BrowseActivity.this,browseDataBase.class);
                    intent.putExtra("String","Notes");
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder alert=new AlertDialog.Builder(BrowseActivity.this);
                    alert.setIcon(R.drawable.alert).setTitle("Error").setMessage("Network Unavailable");
                    AlertDialog dialog=alert.create();
                    dialog.show();
                }
            }
        });
        cvTut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    Intent intent=new Intent(BrowseActivity.this,browseDataBase.class);
                    intent.putExtra("String","Tutorials");
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder alert=new AlertDialog.Builder(BrowseActivity.this);
                    alert.setIcon(R.drawable.alert).setTitle("Error").setMessage("Network Unavailable");
                    AlertDialog dialog=alert.create();
                    dialog.show();
                }
            }
        });
        cvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    Intent intent=new Intent(BrowseActivity.this,AnnapuraMenu.class);
                    intent.putExtra("String","Tutorials");
                    startActivity(intent);
                }
                else{
                    AlertDialog.Builder alert=new AlertDialog.Builder(BrowseActivity.this);
                    alert.setIcon(R.drawable.alert).setTitle("Error").setMessage("Network Unavailable");
                    AlertDialog dialog=alert.create();
                    dialog.show();
                }
            }
        });
        cvDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFolder();
                Intent intent =new Intent(BrowseActivity.this,Download.class);
                startActivity(intent);
            }
        });
        //rv= (RecyclerView) findViewById(R.id.rv);
        /*arrList=new ArrayList<>();
        arrList.add(new mainPagePojo("Notes"));
        arrList.add(new mainPagePojo("Tutorials"));
        arrList.add(new mainPagePojo("Annapurna Menu"));
        arrList.add(new mainPagePojo("Previous Year Paper"));
        Populate p=new Populate();
//        rv.setLayoutManager(new LinearLayoutManager(this));
  //      rv.setAdapter(p);


        *//*mDatabaseReference= FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(BrowseActivity.this, "HELLO WORLD", Toast.LENGTH_SHORT).show();
                for(DataSnapshot a: dataSnapshot.getChildren() ){
                    Toast.makeText(BrowseActivity.this, "HELLO "+ a.getKey(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onDataChange: "+a.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(BrowseActivity.this, "ERRRR", Toast.LENGTH_SHORT).show();
            }
        });*/
    }
    public class VH extends RecyclerView.ViewHolder{
        CardView cv;
        TextView tv;
        ImageView iv;
        public VH(View itemView) {
            super(itemView);
            cv= (CardView) itemView.findViewById(R.id.cardView);
            tv= (TextView) itemView.findViewById(R.id.tv);
            iv= (ImageView) itemView.findViewById(R.id.iv);
        }
    }
    public class Populate extends RecyclerView.Adapter<VH>{

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=getLayoutInflater();
            View v=inflater.inflate(R.layout.mainpage,parent,false);

            return new VH(v);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            final mainPagePojo a=arrList.get(position);
            holder.tv.setText(a.getName());
            holder.iv.setImageResource(a.getIcon());

                holder.cv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(isNetworkAvailable()) {
                            if(a.getName().equals("Notes")||a.getName().equals("Tutorials")) {
                                Intent intent = new Intent(BrowseActivity.this, browseDataBase.class);
                                intent.putExtra("String", a.getName());
                                startActivity(intent);
                            }
                            else if(a.getName().equals("Annapurna Menu")){
                                Intent intent=new Intent(BrowseActivity.this,AnnapuraMenu.class);
                                startActivity(intent);
                            }
                            Log.d(TAG, "onClick: " + "netork connected");
                        }
                        else{
                            AlertDialog.Builder alert=new AlertDialog.Builder(BrowseActivity.this);
                            alert.setIcon(R.drawable.alert).setTitle("Error").setMessage("Network Unavailable");
                            AlertDialog dialog=alert.create();
                            dialog.show();
                        }
                    }
                });

        }

        @Override
        public int getItemCount() {
            return arrList.size();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo= manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo!=null && networkInfo.isConnected()){
            isAvailable=true;
        }
        return isAvailable;
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
}
