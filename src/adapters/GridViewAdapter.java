package adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import classes.GridItem;

import com.everyme.LoveGraffiti.R;

public class GridViewAdapter extends ArrayAdapter<GridItem>{
     private Context context;
     private List<GridItem> data;
	
	public GridViewAdapter(Context context, ArrayList<GridItem> data) {
		super(context, R.layout.row_grid, data);
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
		View row = inflater.inflate(R.layout.row_grid, parent, false);

		ImageView image = (ImageView) row.findViewById(R.id.imageGrid);
		image.setImageBitmap(data.get(position).getImage()); 
		
		return row;
	}
	
}
