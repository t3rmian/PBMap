package io.github.t3r1jj.pbmap.about;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.widget.Toast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.github.t3r1jj.pbmap.R;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
public class OnRateClickListenerTest {

    private boolean verifiedToastCreation = false;
    private boolean verifiedToastCall = false;

    @Test
    @PrepareForTest(Toast.class)
    public void onClick() {
        mockStatic(Toast.class);
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        OnRateClickListener clickListener = new OnRateClickListener(context);
        doAnswer(invocation -> {
            throw new ActivityNotFoundException();
        }).when(context).startActivity(any());
        when(Toast.makeText(context, R.string.could_not_open_android_market, Toast.LENGTH_SHORT)).thenAnswer((ans) -> {
            verifiedToastCreation = true;
            Toast toastMock = mock(Toast.class);
            doAnswer((ans2) -> {
                verifiedToastCall = true;
                return null;
            }).when(toastMock).show();
            return toastMock;
        });

        clickListener.onClick(null);
        assertTrue(verifiedToastCreation);
        assertTrue(verifiedToastCall);
    }
}