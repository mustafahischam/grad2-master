package com.sasa.project.grad2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup;

import com.sasa.project.grad2.Firebase_DataObjects.SDL_DataObjects;

import java.util.List;


public class ViewHolder_SDL extends RecyclerView.Adapter<ViewHolder_SDL.Viewholder_sdl_layout> {
    private List<SDL_DataObjects> sdl_dataObjects;
    // 3shan ye adapt eldata 3la 7asb el Maps api b3taha ka source w location
    public ViewHolder_SDL(List<SDL_DataObjects> sdl_dataObjects1) {
        sdl_dataObjects = sdl_dataObjects1;
    }

    //// https://stackoverflow.com/questions/26245139/how-to-create-recyclerview-with-multiple-view-type creating veiw holder viewtype 3shan el view objects
    public Viewholder_sdl_layout onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_information_activity_sdl, parent, false);
        return new Viewholder_sdl_layout(view);
    }
    /// est3ml el unused viewholder 3shan el posts dont load o memory
    // make el y msh open unused w fadya
    public void onBindViewHolder(Viewholder_sdl_layout holder, int position) {
        holder.recycler(sdl_dataObjects.get(position));
    }
    public int getItemCount() {

        return sdl_dataObjects.size();
    }

    public class Viewholder_sdl_layout extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView User_org_icon;;
        private TextView User_name,Position_title,Time,Route_text;

        public Viewholder_sdl_layout(View itemView) {
            super(itemView);


            Time = itemView.findViewById(R.id.time);
            User_org_icon = itemView.findViewById(R.id.user_org_icon);
            User_name = itemView.findViewById(R.id.post_creator_name);
            Position_title = itemView.findViewById(R.id.position_title);
            Route_text = itemView.findViewById(R.id.route_text);
            itemView.setOnClickListener(this);
        }

        public void recycler(SDL_DataObjects sourcedestinationlocationlist) {

            Position_title.setText(sourcedestinationlocationlist.text);
            User_name.setText(sourcedestinationlocationlist.User_name);
            Time.setText(sourcedestinationlocationlist.time);


            String preAddressString = "";
            if (sourcedestinationlocationlist.type.equals("driver")) {
                preAddressString = "From ";
                User_name.setText(sourcedestinationlocationlist.User_name);
                User_org_icon.setBackgroundResource(R.drawable.pp);
            }
            else if (sourcedestinationlocationlist.type.equals("passenger")) {
                preAddressString = "pick him from ";
                User_name.setText(sourcedestinationlocationlist.User_name);
                User_org_icon.setBackgroundResource(R.drawable.pp);
            }
            else if (sourcedestinationlocationlist.type.equals("destination")) {
                preAddressString = "";
                User_name.setVisibility(View.INVISIBLE);
                // destination gambha heya el organization ely homa el 2 moshtarken feha
                User_org_icon.setBackgroundResource(R.drawable.ic_account_balance_black_24dp);
            }

            String addressString = preAddressString + sourcedestinationlocationlist.address;
            Route_text.setText(addressString);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
