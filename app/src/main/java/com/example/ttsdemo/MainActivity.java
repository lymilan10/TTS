package com.example.ttsdemo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    TextToSpeech mSpeech = null;
    private EditText ed;
    private Button btn_generate, btn_incre_volume, btn_decre_volume, btn_incre_tone, btn_decre_tone, btn_incre_speed, btn_decre_speed;
    private RadioButton English, Chinese, Japanese;
    private RadioGroup languageGroup;
    //法语
    private String strFrench = "Bonjour";
    //意大利语
    private String strItalian = "Ciao";
    //德语
    private String strGermany = "Hallo";
    //西班牙语
    private String strSpan = "Hola";

    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 判断是否是第一次开启应用
        boolean isFirstOpen = SpUtils.getBoolean(this, AppConstants.FIRST_OPEN);
//        Log.d(TAG, "onCreate: " + isFirstOpen);
        System.out.println("onCreate: " + isFirstOpen);
        // 如果是第一次启动，则先进入功能引导页
        if (!isFirstOpen) {
            Intent intent = new Intent(this, AppIntroActivity.class);
            startActivity(intent);
            finish();
            return;
        }



        initLanguageList();

        mSpeech = new TextToSpeech(MainActivity.this, this);
        //设置语音参数
        HashMap ttsOptions = new HashMap<String, String>();

        ed = findViewById(R.id.ed);

        //生成语音
        btn_generate = findViewById(R.id.btn);
        btn_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale language = SharedData.languageList.get(getLanguage(languageGroup));
                if (mSpeech.isLanguageAvailable(language) == TextToSpeech.LANG_AVAILABLE) {
                    mSpeech.getMaxSpeechInputLength();//最大播报文本长度
                    //设置音调,值越大声音越尖（女生），值越小则变成男声,1.0是常规
                    mSpeech.setPitch(SharedData.tone);
                    // 设定语速,默认1.0正常语速
                    mSpeech.setSpeechRate(SharedData.speed);
                    //设置语言
                    mSpeech.setLanguage(language);
                    //默认引擎
                    mSpeech.getDefaultEngine();
                    long utteranceId = System.currentTimeMillis();
                    ttsOptions.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
                            String.valueOf(utteranceId));//utterance，这个参数随便写，用于监听播报完成的回调中
                    ttsOptions.put(TextToSpeech.Engine.KEY_PARAM_VOLUME, String.valueOf(SharedData.volume));//音量
                    ttsOptions.put(TextToSpeech.Engine.KEY_PARAM_STREAM,
                            String.valueOf(AudioManager.STREAM_NOTIFICATION));//播放类型
                    mSpeech.speak(ed.getText().toString(), TextToSpeech.QUEUE_FLUSH, ttsOptions);
                }
            }
        });


        English = findViewById(R.id.language_English);
        Chinese = findViewById(R.id.language_Chinese);
        Japanese = findViewById(R.id.language_Japanese);
        languageGroup = findViewById(R.id.language_group);

        //提升语速
        btn_incre_speed = findViewById(R.id.incre_speed);
        btn_incre_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increSpeed();
            }
        });

        //降低语速
        btn_decre_speed = findViewById(R.id.decre_speed);
        btn_decre_speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreSpeed();
            }
        });

        //提升语调
        btn_incre_tone = findViewById(R.id.incre_tone);
        btn_incre_tone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increTone();
            }
        });

        btn_decre_tone = findViewById(R.id.decre_tone);
        btn_decre_tone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreTone();
            }
        });

        //提高音量
        btn_incre_volume = findViewById(R.id.incre_volume);
        btn_incre_volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increVolume();
            }
        });

        //降低音量
        btn_decre_volume = findViewById(R.id.decre_volume);
        btn_decre_volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreVolume();
            }
        });


        mSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                //播报开始
            }

            @Override
            public void onDone(String utteranceId) {
                //播报结束
            }

            @Override
            public void onError(String utteranceId) {
                //播报出错
            }
        });
    }

    public void increSpeed() {
        if (SharedData.speed >= 2f) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("warning");
            dialog.setMessage("已达最大语速");
            dialog.show();
        } else {
            SharedData.speed += 0.1f;
        }
    }

    public void decreSpeed() {
        if (SharedData.speed <= 0f) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("warning");
            dialog.setMessage("已达最小语速");
            dialog.show();
        } else {
            SharedData.speed -= 0.1f;
        }
    }

    public void increTone() {
        if (SharedData.tone >= 2f) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("warning");
            dialog.setMessage("已达最大音调");
            dialog.show();
        } else {
            SharedData.tone += 0.1f;
        }
    }

    public void decreTone() {
        if (SharedData.tone <= 0.1f) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("warning");
            dialog.setMessage("已达最小音调");
            dialog.show();
        } else {
            SharedData.tone -= 0.1f;
        }
    }

    public void increVolume() {
        if (SharedData.volume >= 1.5f) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("warning");
            dialog.setMessage("已达最大音量");
            dialog.show();
        } else {
            SharedData.volume += 0.1f;
        }
    }

    public void decreVolume() {
        if (SharedData.volume <= 0.1f) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("warning");
            dialog.setMessage("已达最小音量");
            dialog.show();
        } else {
            SharedData.volume -= 0.1f;
        }
    }

    private String getLanguage(RadioGroup languageGroup) {
        int radioButtonId = languageGroup.getCheckedRadioButtonId();
        if (radioButtonId == Chinese.getId()) {
            return "中文";
        } else if (radioButtonId == English.getId()) {
            return "英文";
        } else if (radioButtonId == Japanese.getId()) {
            return "日文";
        }
        return "";
    }

    private void initLanguageList() {
        SharedData.languageList.put("中文", Locale.CHINESE);
        SharedData.languageList.put("英文", Locale.ENGLISH);
        SharedData.languageList.put("日文", Locale.JAPANESE);
    }

    @Override
    public void onInit(int status) {
//        if (mSpeech != null) {
//            int isSupportChinese = mSpeech.isLanguageAvailable(Locale.CHINESE);//是否支持中文
//            mSpeech.getMaxSpeechInputLength();//最大播报文本长度
////            //设置音调,值越大声音越尖（女生），值越小则变成男声,1.0是常规
////            mSpeech.setPitch(0.1f);
////            // 设定语速,默认1.0正常语速
////            mSpeech.setSpeechRate(10.0f);
//
//            if (isSupportChinese == TextToSpeech.LANG_AVAILABLE) {
//                int setLanRet = mSpeech.setLanguage(Locale.CHINESE);//设置语言
//                int setSpeechRateRet = mSpeech.setSpeechRate(1.0f);//设置语
////                int setPitchRet = mSpeech.setPitch(1.0f);//设置音量
//                String defaultEngine = mSpeech.getDefaultEngine();//默认引擎
//
        if (status == TextToSpeech.SUCCESS) {
            //初始化TextToSpeech引擎成功，初始化成功后才可以play等
            System.out.println("TTS引擎初始化成功！");
        } else {
            System.out.println("TTS引擎初始化失败！");
        }
//            }
//        } else {
//            //初始化TextToSpeech引擎失败
//        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}