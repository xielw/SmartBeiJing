package domain;

import java.util.ArrayList;

import android.R.integer;

public class NewsMenu {

	public int retcode;
	public ArrayList<Integer> extend;
	public ArrayList<NewsMenuData> data;
	
	public class NewsMenuData{
		
		public int id;
		public String title;
		public String type;
		public ArrayList<NewsTabData> children;
		@Override
		public String toString() {
			return "NewsMenuData [title=" + title + ", children=" + children
					+ "]";
		}
		
	}
	
	public class NewsTabData{
		public int id;
		public String title;
		public String type;
		public String url;
		
		@Override
		public String toString() {
			return "NewsTabData [title=" + title + "]";
		}
	}

	
	@Override
	public String toString() {
		return "NewsMenu [data=" + data + "]";
	}
	
	
}
