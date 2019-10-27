
package com.example.jaspreet.sportsfest;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class QListViewAdapter extends BaseAdapter {
    Activity mcontext;
    ArrayList<String> qcontent;
    ArrayList<String> qrate;
    ArrayList<String> qid;
    ArrayList<String> qtype;


    public QListViewAdapter(MainActivity context, ArrayList<String> arrayList, ArrayList<String> arrayList1, ArrayList<String> arrayList2, ArrayList<String> arrayList3) {
        mcontext=context;
        qid=arrayList;
        qcontent=arrayList1;
        qrate=arrayList2;
        qtype=arrayList3;

    }





    @Override
    public int getCount() {

        return qid.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder{
        TextView txtviewtqid;
        TextView txtviewqcontent;
        TextView txtqrate;
        TextView txtqtype;


    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;


        LayoutInflater inflater=mcontext.getLayoutInflater();
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.myfriendslist,null);
            holder=new ViewHolder();
            holder.txtviewtqid=(TextView)convertView.findViewById(R.id.textView68);

            holder.txtviewqcontent=(TextView)convertView.findViewById(R.id.textView69);
            holder.txtqrate=(TextView)convertView.findViewById(R.id.textView3);
            holder.txtqtype=(TextView)convertView.findViewById(R.id.type);

            String st="QID: "+ qid.get(position) ;
            holder.txtviewtqid.setText(st);
            holder.txtviewqcontent.setText(qcontent.get(position)+"?");
            String str="Reg. Fee: "+qrate.get(position)+" COS";
            holder.txtqrate.setText(str);
            if(qtype.get(position).equals("1")){
                holder.txtqtype.setText("INT");
            }else{
                holder.txtqtype.setText("MCQ");
            }



        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }




        return convertView;
    }



}

