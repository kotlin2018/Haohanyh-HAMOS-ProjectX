// 受Haohanyh Computer Software Products Open Source LICENSE保护 https://git.haohanyh.top:3001/Haohanyh/LICENSE
package com.haohanyh.hamos.projectx.ImportActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.haohanyh.hamos.projectx.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SecondActivity extends Activity {

    private String HUAWEITOKEN = "";
    public TextView txtLuminance;
    public TextView txtLightStatus;

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Button btnnew = findViewById(R.id.kaiqi);
        txtLuminance = findViewById(R.id.txtlight);
        txtLightStatus = findViewById(R.id.txtstatus);

        btnnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnnew.isClickable()) { //如果是可以点击的，则执行方法
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() { Huawei(); }
                    };
                    timer.schedule(task, 0, 1000);
                    Toast.makeText(SecondActivity.this, "开始读取福州智能数据机房华为IotA接口的光照传感器数值", Toast.LENGTH_SHORT).show();
                    btnnew.setClickable(false);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    void Huawei() {
        post();
        String result = get("https://iotda.cn-north-4.myhuaweicloud.com/v5/iot/0d119fff0980102a2fb1c010bcd3cc73/devices/625d28cfecf9c41c38216abe_2454refwefw425/shadow");
        try {
            JSONObject jsonObj = new JSONObject(result);
            System.out.println("浩瀚银河Huawei函数灰度测试:result=====" + jsonObj);
            JSONArray jsonArray = jsonObj.getJSONArray("shadow");
            System.out.println("浩瀚银河Huawei函数灰度测试:shadow=====" + jsonArray);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                System.out.println("浩瀚银河Huawei函数灰度测试:reported=====");
                Object reported = obj.get("reported");
                System.out.println(reported);

                JSONObject Two = new JSONObject(String.valueOf(reported));
                String properties = Two.getString("properties");
                System.out.println("浩瀚银河Huawei函数灰度测试:properties=====" + properties);

                JSONObject Lumi = new JSONObject(properties);
                String Lumiresult = Lumi.getString("Luminance");
                System.out.println("浩瀚银河Huawei函数灰度测试:Luminance=====" + Lumiresult);

                JSONObject Light = new JSONObject(properties);
                String Lightresult = Light.getString("LightStatus");
                System.out.println("浩瀚银河Huawei函数灰度测试:Luminance=====" + Lightresult);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        txtLuminance.setText("当前光照值：" + "\n\t" + Lumiresult);

                        String str201 = "ON";
                        String str202 = "OFF";
                        if (Lightresult.equals(str201)) {
                            txtLightStatus.setText("是否开灯： " + "\n\t" + "开着呢");
                        } else if (Lightresult.equals(str202)) {
                            txtLightStatus.setText("是否开灯： " + "\n\t" + "关了呢");
                        } else {
                            txtLightStatus.setText("当前状态： " + "\n\t" + Lightresult);
                        }

                    }
                });



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String get(String url) {
        String content = "";
        URLConnection urlConnection = null;
        try {
            urlConnection = new URL(url).openConnection();
            HttpURLConnection connection = (HttpURLConnection) urlConnection;
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("X-Auth-Token", HUAWEITOKEN);
            //连接
            connection.connect();
            int responseCode = connection.getResponseCode();
            System.out.println("浩瀚银河Get函数灰度测试:responseCode=====" + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder bs = new StringBuilder();
                String l;
                while ((l = bufferedReader.readLine()) != null) {
                    bs.append(l).append("\n");
                }
                content = bs.toString();
            } else if (responseCode == 401) {
                System.out.println("浩瀚银河Get函数灰度测试:failed");
            }
            //System.out.println("浩瀚银河Get函数灰度测试:content=====" + content);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private void post() {
        String jsonwenben = "{\"auth\":{\"identity\":{\"methods\":[\"password\"],\"password\":{\"user\":{\"domain\":{\"name\":\"zhonghuayouwei_guozhironyao\"},\"name\":\"haohanyhhuawei\",\"password\":\"minecraft0305\"}}},\"scope\":{\"domain\":{\"name\":\"zhonghuayouwei_guozhironyao\"}}}}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonwenben);
        Request request = new Request.Builder()
                .url("https://iam.cn-north-4.myhuaweicloud.com/v3/auth/tokens")
                .post(body)
                .build();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("浩瀚银河post函数灰度测试: " + e.getLocalizedMessage() + "，灰度测试失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("浩瀚银河post函数链接灰度测试: " + response.body().string());
                System.out.println("浩瀚银河post函数头部读取灰度测试: " + response.header("x-subject-token"));
                HUAWEITOKEN = response.header("x-subject-token");
            }
        });
    }
}
