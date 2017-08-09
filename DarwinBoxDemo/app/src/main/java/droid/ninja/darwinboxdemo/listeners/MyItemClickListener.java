package droid.ninja.darwinboxdemo.listeners;

import android.view.View;

/**
 * Created by mukesh on 8/7/17.
 */

public interface MyItemClickListener {


    public interface OnItemClickListener{

        public void OnItemClick(View view, int position);

    }

    void setOnItemClickListener(OnItemClickListener mListener);

}
