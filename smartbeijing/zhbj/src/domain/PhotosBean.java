package domain;

import java.util.ArrayList;

/**
 * 组图对象
 * @author xielianwu
 *
 */
public class PhotosBean {

	public PhotosData data;
	public class PhotosData{
		
		public String more;
		public ArrayList<PhotosNews> news;
		
	}
	
	public class PhotosNews{
		
		public int  id;
		public String listimage;
		public String title;
	}
}
