package com.zmbdp.alibaba;


//import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioSpeechModel;
//import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioSpeechOptions;
//import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisModel;
//import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisOptions;
//import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisModel;
//import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisOptions;
//import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisModel;
//import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
//import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioSpeechModel;
import com.alibaba.cloud.ai.dashscope.audio.DashScopeAudioSpeechOptions;
import com.alibaba.dashscope.audio.tts.SpeechSynthesisAudioFormat;
import com.alibaba.dashscope.audio.tts.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.tts.SpeechSynthesizer;
import org.junit.jupiter.api.Test;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 音频模型测试
 *
 * @author 稚名不带撇
 */
@SpringBootTest
public class AudioModelTest {
    private final String TEXT = "白日依山尽，黄河入海流。欲穷千里目，更上一层楼";

    // ------------------------------------------- 下面是 1.1.2.0 版本 ----------------
    @Autowired
    private DashScopeAudioSpeechModel speechSynthesisModel;

    @Test
    void tts() {
        TextToSpeechResponse response = speechSynthesisModel.call(new TextToSpeechPrompt(TEXT));
        //把模型返回结果, 写入到文件中
        File file = new File(System.getProperty("user.dir") + "/out.mp3");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] audio = response.getResult().getOutput();
            fos.write(audio);
        } catch (IOException e) {
            System.out.println("写入文件失败");
        }
    }

    @Test
    void tts2() {
        DashScopeAudioSpeechOptions options = DashScopeAudioSpeechOptions.builder()
                .model("cosyvoice-v3-flash")
                .voice("longhuhu_v3")
                .speed(0.5)  //语速
                .pitch(1.5D)  //音调
                .volume(10)
                .build();
        TextToSpeechResponse response = speechSynthesisModel.call(new TextToSpeechPrompt(TEXT, options));
        //把模型返回结果, 写入到文件中
        File file = new File(System.getProperty("user.dir") + "/out.mp3");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] audio = response.getResult().getOutput();
            fos.write(audio);
        } catch (IOException e) {
            System.out.println("写入文件失败");
        }
    }
    // ------------------------------------------- 1.0.0.2 ----------------

    // ------------------------------------------- 下面是 1.0.0.2 版本 ----------------

//    @Autowired
//    private DashScopeSpeechSynthesisModel speechSynthesisModel;
//
//    private final String TEXT = "白日依山尽，黄河入海流。这是测试";
//
//    @Test
//    void tts(){
//        SpeechSynthesisPrompt prompt = new SpeechSynthesisPrompt(TEXT);
//        SpeechSynthesisResponse response = speechSynthesisModel.call(prompt);
//        //把模型返回结果, 写入到文件中
//        File file = new File(System.getProperty("user.dir")+"/out.mp3");
//        try(FileOutputStream fos = new FileOutputStream(file)) {
//            ByteBuffer audio = response.getResult().getOutput().getAudio();
//            fos.write(audio.array());
//        }catch (IOException e){
//            System.out.println("写入文件失败");
//        }
//    }
//
//    @Test
//    void tts2(){
//        //1.0.0.2
//        DashScopeSpeechSynthesisOptions options = DashScopeSpeechSynthesisOptions.builder()
//                .model("cosyvoice-v3-flash")
//                .voice("longhuhu_v3")
//                .speed(0.5f)  //语速
//                .pitch(1.5D)  //音调
//                .volume(10)
//                .build();
//        SpeechSynthesisPrompt prompt = new SpeechSynthesisPrompt(TEXT, options);
//        SpeechSynthesisResponse response = speechSynthesisModel.call(prompt);
//        //把模型返回结果, 写入到文件中
//        File file = new File(System.getProperty("user.dir")+"/out.mp3");
//        try(FileOutputStream fos = new FileOutputStream(file)) {
//            ByteBuffer audio = response.getResult().getOutput().getAudio();
//            fos.write(audio.array());
//        }catch (IOException e){
//            System.out.println("写入文件失败");
//        }
//    }
    // ------------------------------------------- 1.1.2.0 ----------------


    @Test
    void text2Audio() {
        SpeechSynthesizer synthesizer = new SpeechSynthesizer();
        SpeechSynthesisParam param = SpeechSynthesisParam.builder()
                // 若没有将API Key配置到环境变量中，需将下面这行代码注释放开，并将apiKey替换为自己的API Key
//                 .apiKey("yourApikey")
                .model("sambert-zhichu-v1")
                .text("今天天气怎么样")
                .sampleRate(48000)
                .format(SpeechSynthesisAudioFormat.WAV)
                .build();

        File file = new File(System.getProperty("user.dir") + "/output.wav");
        // 提交同步合成任务，获取完整的音频数据
        ByteBuffer audio = synthesizer.call(param);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(audio.array());
            System.out.println("synthesis done!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}