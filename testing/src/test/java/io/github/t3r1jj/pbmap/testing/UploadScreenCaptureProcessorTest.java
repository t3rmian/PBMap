package io.github.t3r1jj.pbmap.testing;

import android.graphics.Bitmap;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import androidx.test.runner.screenshot.ScreenCapture;
import androidx.test.runner.screenshot.Screenshot;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {Log.class, Screenshot.class, ServiceGenerator.class})
@PowerMockIgnore("javax.net.ssl.*")
public class UploadScreenCaptureProcessorTest {

    @Mock
    private Call<ResponseBody> callMock;
    @Mock
    private Response<ResponseBody> responseMock;
    @Mock
    private ResponseBody responseBodyMock;
    @Mock
    private ScreenCapture screenCaptureMock;
    @Mock
    private Bitmap bitmapMock;

    @Test(expected = MockedOutcome.class)
    public void process_successful_noBody() throws IOException {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(ServiceGenerator.class);
        UploadService serviceMock = mock(UploadService.class);
        when(ServiceGenerator.createService(any())).thenReturn(serviceMock);
        when(serviceMock.upload(any())).thenReturn(callMock);
        when(screenCaptureMock.getBitmap()).thenReturn(bitmapMock);
        when(callMock.execute()).thenReturn(responseMock);
        when(responseMock.message()).thenReturn("msg");
        when(responseMock.isSuccessful()).thenReturn(true);

        UploadScreenCaptureProcessor processor = new UploadScreenCaptureProcessor();
        when(Log.println(eq(Log.INFO), eq(UploadScreenCaptureProcessor.class.getSimpleName()), eq("msg")))
                .thenThrow(MockedOutcome.class);
        processor.process(screenCaptureMock);
    }

    @Test(expected = MockedOutcome.class)
    public void process_unsuccessful_noBody() throws IOException {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(ServiceGenerator.class);
        UploadService serviceMock = mock(UploadService.class);
        when(ServiceGenerator.createService(any())).thenReturn(serviceMock);
        when(serviceMock.upload(any())).thenReturn(callMock);
        when(screenCaptureMock.getBitmap()).thenReturn(bitmapMock);
        when(callMock.execute()).thenReturn(responseMock);
        when(responseMock.message()).thenReturn("msg");
        when(responseMock.isSuccessful()).thenReturn(false);

        UploadScreenCaptureProcessor processor = new UploadScreenCaptureProcessor();
        when(Log.println(eq(Log.ERROR), eq(UploadScreenCaptureProcessor.class.getSimpleName()), eq("msg")))
                .thenThrow(MockedOutcome.class);
        processor.process(screenCaptureMock);
    }

    @Test(expected = MockedOutcome.class)
    public void process_successful() throws IOException {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(ServiceGenerator.class);
        UploadService serviceMock = mock(UploadService.class);
        when(ServiceGenerator.createService(any())).thenReturn(serviceMock);
        when(serviceMock.upload(any())).thenReturn(callMock);
        when(screenCaptureMock.getBitmap()).thenReturn(bitmapMock);
        when(callMock.execute()).thenReturn(responseMock);
        when(responseMock.body()).thenReturn(responseBodyMock);
        when(responseBodyMock.string()).thenReturn("body");
        when(responseMock.isSuccessful()).thenReturn(true);

        UploadScreenCaptureProcessor processor = new UploadScreenCaptureProcessor();
        when(Log.println(eq(Log.INFO), eq(UploadScreenCaptureProcessor.class.getSimpleName()), eq("body")))
                .thenThrow(MockedOutcome.class);
        processor.process(screenCaptureMock);
    }

    @Test(expected = MockedOutcome.class)
    public void process_unsuccessful() throws IOException {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(ServiceGenerator.class);
        UploadService serviceMock = mock(UploadService.class);
        when(ServiceGenerator.createService(any())).thenReturn(serviceMock);
        when(serviceMock.upload(any())).thenReturn(callMock);
        when(screenCaptureMock.getBitmap()).thenReturn(bitmapMock);
        when(callMock.execute()).thenReturn(responseMock);
        when(responseMock.errorBody()).thenReturn(responseBodyMock);
        when(responseBodyMock.string()).thenReturn("body");
        when(responseMock.isSuccessful()).thenReturn(false);

        UploadScreenCaptureProcessor processor = new UploadScreenCaptureProcessor();
        when(Log.println(eq(Log.ERROR), eq(UploadScreenCaptureProcessor.class.getSimpleName()), eq("body")))
                .thenThrow(MockedOutcome.class);
        processor.process(screenCaptureMock);
    }

}