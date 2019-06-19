package envy.makers2019.envyprj;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class dialog_verify extends DialogFragment {

    public static final String TAG_EVENT_DIALOG = "verify_dialog_event";

    public dialog_verify() {
    }

    public static dialog_verify getInstance() {
        dialog_verify e = new dialog_verify();
        return e;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog_verify, container);
        Button Submit = (Button) v.findViewById(R.id.submit);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                dismiss();
            }
        });
        return v;
    }
}
