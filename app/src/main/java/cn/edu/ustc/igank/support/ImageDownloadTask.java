package cn.edu.ustc.igank.support;

import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import cn.edu.ustc.igank.R;

/**
 * Created by lehman on 2016/11/18.
 */

public class ImageDownloadTask extends Thread {
    private String url;
    private View view;
    public ImageDownloadTask(String url, View view) {
        this.url=url;
        this.view=view;
    }

    @Override
    public void run() {
        super.run();
        try
        {
            URL url = new URL(this.url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            String SDCardRoot = Environment.getExternalStorageDirectory().toString();
            String filename=System.currentTimeMillis()+".png";
            Log.i("Local filename:",""+filename);
            File filedir = new File(SDCardRoot+"/IGank");
            if (!filedir.exists()){
                filedir.mkdir();
            }
            File file = new File(SDCardRoot+"/IGank/"+filename);
            Log.e("filename",file.getAbsolutePath());
            if(file.createNewFile())
            {
                file.createNewFile();
            }
            FileOutputStream fileOutput = new FileOutputStream(file);
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            int downloadedSize = 0;
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ( (bufferLength = inputStream.read(buffer)) > 0 )
            {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                Log.i("Progress:","downloadedSize:"+downloadedSize+"totalSize:"+ totalSize) ;
            }
            fileOutput.close();
            Snackbar.make(view, R.string.image_download, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }



//        int count;
//        try {
//            URL url = new URL(this.url);
//            URLConnection conection = url.openConnection();
//            conection.connect();
//
//            // this will be useful so that you can show a tipical 0-100%
//            // progress bar
//            int lenghtOfFile = conection.getContentLength();
//
//            // download the file
//            InputStream input = new BufferedInputStream(url.openStream(),
//                    8192);
//
//            // Output stream
//            OutputStream output = new FileOutputStream(Environment
//                    .getExternalStorageDirectory().toString()
//                    + "/igank/www.png");
//
//            byte data[] = new byte[1024];
//
//            long total = 0;
//
//            while ((count = input.read(data)) != -1) {
//                total += count;
//                // writing data to file
//                output.write(data, 0, count);
//            }
//
//            // flushing output
//            output.flush();
//
//            // closing streams
//            output.close();
//            input.close();
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
//
//        } catch (Exception e) {
//            Log.e("Error: ", e.getMessage());
//        }
    }
}
