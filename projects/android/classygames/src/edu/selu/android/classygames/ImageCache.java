package edu.selu.android.classygames;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.koushikdutta.urlimageviewhelper.DiskLruCache;
import com.koushikdutta.urlimageviewhelper.DiskLruCache.Snapshot;

import edu.selu.android.classygames.utilities.Utilities;

public class ImageCache
{
	
	
	public DiskLruCache mDiskCache;
	public LruCache<Long, Bitmap> mMemoryCache;
	
	public final static CompressFormat COMPRESS_FORMAT = CompressFormat.PNG;
    public final static int COMPRESS_QUALITY = 70;
	public final static int APP_VERSION = 1;
	public final static int VALUE_COUNT = 1;
	public final static int IO_BUFFER_SIZE = 8 * 1024;
	public final static int DISK_CACHE_SIZE = 1024 * 1024 * 10;
	public final static String DISK_CACHE_SUBDIR = "thumbnails";
	
	
	public static int getCacheSize(int memClass)
	{
		// currently use 1/8 of available memory for the cache
		int cacheSize = 1024 * 1024 * memClass / 8;
		
		return cacheSize;
	}
	
	
	public static void addBitmapToCache(Long key, Bitmap bitmap, LruCache<Long, Bitmap> memoryCache, DiskLruCache diskCache)
	{	
		//Add to memory cache
		if(getBitmapFromMemCache(key, memoryCache) == null)
		{
			memoryCache.put(key, bitmap);
		}
		
		//Add to disk cache as well
		if(!containsKey(key.toString(), diskCache))
		{
			put(key.toString(), bitmap, diskCache);
		}
		
	}
	
	
	public static Bitmap getBitmapFromMemCache(Long key, LruCache<Long, Bitmap> memoryCache)
	{
		return (Bitmap) memoryCache.get(key);
	}
	
	
	public static Bitmap getBitmapFromDiskCache(Long key, DiskLruCache diskCache) 
	{
		final InputStream in;
		final BufferedInputStream bufferIn;
		Snapshot snapshot = null;
		Bitmap bitmap = null;
		
		try
		{
			snapshot = diskCache.get(key.toString());
			
			if(snapshot == null)
			{
				return null;
			}
			
			in = snapshot.getInputStream(0);
			
			if(in != null)
			{
				bufferIn = new BufferedInputStream(in, IO_BUFFER_SIZE);
				bitmap = BitmapFactory.decodeStream(bufferIn);
			}
		}
		catch(IOException e)
		{
			Log.e(Utilities.LOG_TAG, "DiskCache get failed: " + e);
			return null;
		}
		
		snapshot.close();
		
	    return bitmap;
	}

	
	public static boolean containsKey(String key, DiskLruCache diskCache) 
	{
        boolean contained = false;
        Snapshot snapshot = null;
        
        try 
        {
            snapshot = diskCache.get( key );
            
            if(snapshot != null)
            {
            	contained = true;
               	snapshot.close();
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
                  

        return contained;
    }
	
	
	public static void put(String key,Bitmap data, DiskLruCache diskCache) {

        DiskLruCache.Editor editor = null;
        
        try 
        {
            editor = diskCache.edit( key );
            if ( editor == null ) 
            {
                return;
            }

            if(writeBitmapToFile(data, editor)) 
            {               
                diskCache.flush();
                editor.commit();
                
                if ( BuildConfig.DEBUG ) 
                {
                   Log.d( "cache_test_DISK_", "image put on disk cache " + key );
                }
            } 
            else 
            {
                editor.abort();
                
                if ( BuildConfig.DEBUG ) 
                {
                    Log.d( "cache_test_DISK_", "ERROR on: ibbbbbmage put on disk cache " + key );
                }
            }   
        } 
        catch (IOException e) 
        {
            if ( BuildConfig.DEBUG ) 
            {
                Log.d( "cache_test_DISK_", "ERROR on: imaaaaage put on disk cache " + key );
            }
            
            try 
            {
                if ( editor != null ) 
                {
                    editor.abort();
                }
            } 
            catch (IOException ignored) 
            {		
            	//Do nothing
            }           
        }

    }
	
	
	public static boolean writeBitmapToFile(Bitmap bitmap, DiskLruCache.Editor editor) throws IOException 
	{
		OutputStream out = null;
	  
		try
		{
			out = new BufferedOutputStream( editor.newOutputStream( 0 ), IO_BUFFER_SIZE );
			return bitmap.compress( COMPRESS_FORMAT, COMPRESS_QUALITY, out );
		}
		finally
		{
			if(out != null)
			{
				out.close();
			}
	    }
	}
		
	
}
