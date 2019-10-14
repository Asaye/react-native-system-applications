package com.sysapps.utils;

import com.sysapps.R;

import android.app.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle; 
import android.os.Environment; 
import android.view.View;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.database.Cursor;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.ArrayList;

import android.util.Log;

public class Table extends Fragment implements View.OnClickListener{

	protected float PAGE_WIDTH = 595 - 2*72;
	protected float PAGE_HEIGHT = 842 - 2*72;
	protected int ROWS_PER_PAGE = 25;	
	
	private Drawable mDrawable = new Border();

	private View mView, mAlertView;
	private TableLayout mLayout;
	private Cursor mCursor;
	protected Context context;
	protected Activity activity;
	protected Cursor cursor;
	protected String fileName = "";
	protected String[] titles = null;	

	private int mRowCount = 0;
	private int mPageNumber = 0;

	private void getCursor() {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	
		mAlertView = inflater.inflate(R.layout.sysapps_alert, container, false);
		return inflater.inflate(R.layout.sysapps_fragment_table, container, false);
	}

	@Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	if (this.fileName == null || this.cursor == null) {
    		return;
    	}

    	mView = view;
    	mCursor = this.cursor;
    	mRowCount = mCursor.getCount();    	

    	try {
    		File path = this.context
    						.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);    
    		File destFile = new File(path, this.fileName);
    		FileOutputStream output = new FileOutputStream(destFile);
	        PdfDocument document = new PdfDocument();  

		    if(!destFile.exists()){
		        destFile.mkdir();
		    }

			showDialog();		
			initializeView();		

	        while(getTabulatedData() != null) {

		        PdfDocument.PageInfo pageInfo = 
		        	new PdfDocument.PageInfo.Builder((int)PAGE_WIDTH, (int)PAGE_HEIGHT, mPageNumber)
		        	.create();
		        PdfDocument.Page page = document.startPage(pageInfo);
		        Canvas pageCanvas = page.getCanvas();	        

		        pageCanvas.translate(72, 72);		        
	            mLayout.measure((int)PAGE_WIDTH, (int)PAGE_HEIGHT);
	            mLayout.layout(0, 0, (int)PAGE_WIDTH, (int)PAGE_HEIGHT);
	            float scalex = PAGE_WIDTH/(PAGE_WIDTH + 2*72);
	            float scaley = PAGE_HEIGHT/(PAGE_HEIGHT + 2*72);
	            float scale = Math.min(scalex, scaley);
	            pageCanvas.scale(scale, scale);
				mLayout.draw(pageCanvas);
		        document.finishPage(page);
		        initializeView();		         
	    	}
	        document.writeTo(output);
	        document.close();
	        String msg = "The document is created and saved as: " + 
	        			 destFile.getAbsolutePath();
	        TextView alertText = (TextView) mAlertView.findViewById(R.id.alertText);
	        alertText.setText(msg);
    	} catch (Exception ex) {
    		
    	}
    }	

    @Override
    public void onClick(View view) {
   		removeFragment();
    } 

    private void initializeView() {
    	if (mLayout != null) {
    		mLayout.removeAllViews();
    		mLayout = null;
    	} 

    	mLayout = (TableLayout) mView.findViewById(R.id.table);
	    mLayout.setStretchAllColumns(true);
    }    

    private TableLayout getTabulatedData() {
    	int n = mPageNumber*ROWS_PER_PAGE;

    	if (mCursor == null || mRowCount <= n) {
    		return null;
    	}

        if (mCursor.moveToPosition(n)) {    	
        	mLayout.addView(getHeader());
        	renderRows(n);
        	mPageNumber++;           
    	}

    	return mLayout; 
    }

	private TableRow getHeader() {
		TableRow header =  new TableRow(this.context);

		for (String title: this.titles) {
			TextView textView = new TextView(this.context);
			textView.setBackground(mDrawable);
			textView.setText(title);
			textView.setTextSize(10);
			textView.setGravity(Gravity.CENTER); 
			header.addView(textView); 
		}

        return header;        
	}	

	private void renderRows(int pageNo) {
		
		int rows = 0;
        do {
           rows++;
           List<String> rowData = new ArrayList<String>();
           rowData.add("" + (pageNo + rows));
           this.getRowData(mCursor, rowData);
	       mLayout.addView(getRow(rowData)); 			            
        } while (mCursor.moveToNext() && rows < ROWS_PER_PAGE);
	}

	protected void getRowData(Cursor cursor, List rowData) {
		
	}

	private TableRow getRow(List rowData) {
		
		TableRow row =  new TableRow(this.context);
		int col = 0;
		for (int i = 0; i < rowData.size(); i++) {
			TextView textView = new TextView(this.context);
			textView.setBackground(mDrawable);
			textView.setText((String) rowData.get(i));
			textView.setTextSize(8);
			textView.setPadding(10, 2, 10, 2);

			if (col == 0) {
				textView.setGravity(Gravity.CENTER);
				col++;
			} 			
			row.addView(textView); 
		}

        return row;        
	}

	private void showDialog() {		
	    AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
	    builder.setCancelable(true); 
	    builder.setView(mAlertView); 

	    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	            removeFragment();
	        }
	    });
	    AlertDialog alertDialog = builder.create();
      	alertDialog.show();
	}

	private void removeFragment() {
		if (getFragmentManager() != null) {
				getFragmentManager().beginTransaction().remove(this)
        				.commitAllowingStateLoss();
		}
	}

	public static class Border extends Drawable {
		private Rect mBounds;

		@Override  
		public void onBoundsChange(Rect bounds) {
			mBounds = bounds;  
   		}

   		@Override 
   		public void draw(Canvas c) { 
   			Paint paint = new Paint();  
			paint.setColor(Color.parseColor("#cccccc"));  
			paint.setStrokeWidth(1);  
			paint.setStyle(Paint.Style.STROKE);   
    		c.drawRect(mBounds, paint);  
   		} 
   		@Override 
   		public int getOpacity() { 
   			 return 1;
   		}

   		@Override 
   		public void setColorFilter(ColorFilter colorFilter) { 
   			 
   		}

   		@Override 
   		public void setAlpha(int alpha) { 
   			 
   		}   
	}
}
