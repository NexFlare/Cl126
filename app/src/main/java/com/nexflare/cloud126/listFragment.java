package com.nexflare.cloud126;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;

/**
 * Created by 15103068 on 28-02-2017.
 */

public class listFragment extends Fragment {
    private static final String FRAGMENT_TAG ="fRAG";
    FragmentManager mFragment;
    FragmentTransaction mFragmentTransaction;
    RecyclerView fragRecyclerView;
    ArrayList<String> arrList;
    fragRVAdapter frag;
    String desire;
    ProgressBar progress;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewGroup=inflater.inflate(R.layout.fraglayout,container,false);
        fragRecyclerView= (RecyclerView) viewGroup.findViewById(R.id.fragRV);
        progress= (ProgressBar) container.findViewById(R.id.progressBar);
        progress.setVisibility(View.VISIBLE);
        arrList=new ArrayList<>();
        mFragment=getFragmentManager();
        Bundle arguments=getArguments();
        desire=arguments.getString("String");
        frag=new fragRVAdapter();

        populateArray(desire);
        fragRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fragRecyclerView.setAdapter(frag);
        return viewGroup;
    }

    private void populateArray(String reference) {
        DatabaseReference mDataBase= FirebaseDatabase.getInstance().getReference(reference);
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progress.setVisibility(View.INVISIBLE);
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Toast.makeText(getActivity(), ds.getKey(), Toast.LENGTH_SHORT).show();
                    arrList.add(ds.getKey());
                }
                frag.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class Holder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView tv;
        public Holder(View itemView) {
            super(itemView);
            cv= (CardView) itemView.findViewById(R.id.cardView);
            tv= (TextView) itemView.findViewById(R.id.tv);
        }
    }
    class fragRVAdapter extends RecyclerView.Adapter<Holder>{

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getContext());
            View rootView=inflater.inflate(R.layout.mainpage,parent,false);
            //Toast.makeText(getActivity(), "HELLO World", Toast.LENGTH_SHORT).show();
            return new Holder(rootView);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            final String s=arrList.get(position);
            //Toast.makeText(getActivity(), " IN "+ s, Toast.LENGTH_SHORT).show();
            holder.tv.setText(s);


            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String ref=desire+"/"+s;
                    //setFragment(ref);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrList.size();
        }
    }
    /*private void setFragment(String s) {
        listFragment ls=new listFragment();
        Bundle arguments = new Bundle();
        arguments.putString("String", s);
        //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        ls.setArguments(arguments);
        mFragmentTransaction=mFragment.beginTransaction();
        mFragmentTransaction.add(R.id.container,ls,FRAGMENT_TAG);
        mFragmentTransaction.commit();
    }*/
}
