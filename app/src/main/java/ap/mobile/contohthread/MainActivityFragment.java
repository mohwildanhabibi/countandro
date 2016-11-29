package ap.mobile.contohthread;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
        implements View.OnClickListener, RandomInterface {

    private TextView textHour;
    private TextView textMinute;
    private TextView textSecond;
    private EditText hour;
    private EditText minute;
    private EditText second;
    private Button buttonStart;
    private Button buttonStop;
    private Integer jam;
    private Integer menit;
    private Integer detik;
    private boolean awal;
    volatile boolean running;

    Runnable runnableBackground = new Runnable() {
        @Override
        public void run() {
            try {
                while(true) {
                    if (awal == true) {
                        jam = Integer.parseInt(hour.getText().toString());
                        menit = Integer.parseInt(minute.getText().toString());
                        detik = Integer.parseInt(second.getText().toString());
                        awal = false;
                    }

                    if (detik == 0) {
                        detik = 59;
                        menit -= 1;
                    } else if (menit == 0) {
                        if (jam > 0) {
                            menit = 59;
                            jam -= 1;
                        }
                    }



                    detik -= 1;


                    // Kirimkan angka random ke Thread UI melalui handler
                    // Runnable yan dijalankan di Thread UI adalah runnableUI
                    handler.post(runnableUI);


                    // Set delay sebelum menggenerate angka lainnya.
                    Thread.sleep(200);
                }

            // Jika terjadi exception, yaitu pada saat sleep tombol stop diklik
            // Telan exceptionnya
            } catch (Exception ex) {}

            // Tidak boleh mengubah/memanipulasi elemen UI
            // dari Thread yang bukan Thread UI
            //textAngka.setText(angka.toString());
        }
    };

    Runnable runnableUI = new Runnable() {
        @Override
        public void run() {
            // ambil angka yang sudah digenerate, ubah ke string
            // dan tampilkan angkanya di TextView
            if (!running) return;
            textSecond.setText(detik.toString());
            textHour.setText(jam.toString());
            textMinute.setText(menit.toString());
            if (detik <= 0 && menit <= 0 && jam <= 0){
                running = false;
                threadBackground.interrupt();
                awal = false;
            }
        }
    };

    // Deklarasi thread
    Thread threadBackground;

    // Deklarasi handler sebagai jembatan antar Thread
    Handler handler = new Handler();

    // Deklarasi objek AsyncTask
    //private RandomTask randomTask;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        this.textMinute = (TextView) rootView.findViewById(R.id.textMinute);
        this.textHour = (TextView) rootView.findViewById(R.id.textHour);
        this.textSecond = (TextView) rootView.findViewById(R.id.textSecond);
        this.hour = (EditText) rootView.findViewById(R.id.hour);
        this.minute = (EditText) rootView.findViewById(R.id.minute);
        this.second = (EditText) rootView.findViewById(R.id.second);
        this.buttonStart = (Button) rootView.findViewById(R.id.button_start);
        this.buttonStop = (Button) rootView.findViewById(R.id.button_stop);

        this.buttonStart.setOnClickListener(this);
        this.buttonStop.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_start:
                // Menggunakan ASyncTask
                /*
                if(this.randomTask == null
                        || this.randomTask.getStatus()
                        == AsyncTask.Status.FINISHED) {
                    // Setiap kali thread akan dijalankan, harus dibuat baru,
                    // Thread yang sudah finish/terminated tidak bisa dijalankan kembali
                    // Membuat objek AsyncTask dan memberikan objek Fragment ini sebagai RandomInterface (this)
                    // Supaya method dalam fragment ini bisa dipanggil dari AsyncTask
                    this.randomTask = new RandomTask(this);

                    // Jalankan Thread (Task)
                    this.randomTask.execute();
                }
                */

                // Menggunakan Thread standar
                if(threadBackground == null
                        || threadBackground.getState() == Thread.State.TERMINATED) {
                    // Buat Thread baru setiap kali tombol start di klik
                    // Setiap kali thread akan dijalankan, harus dibuat baru,
                    // Thread yang sudah finish/terminated tidak bisa dijalankan kembali
                    threadBackground = new Thread(runnableBackground);
                    awal = true;
                    running = true;
                    // Jalankan Thread
                    threadBackground.start();
                }

                break;
            case R.id.button_stop:
                // Menggunakan thread standar

                // Hentikan proses Thread background
                 threadBackground.interrupt();
                awal = false;
                /*
                // Menggunakan AsyncTask
                if(this.randomTask.getStatus() == AsyncTask.Status.RUNNING)
                    // Hentikan proses di Thread background
                    this.randomTask.cancel(true);
                */
                break;
        }
    }

    @Override
    public void onAngkaDidapat(Integer angka) {
        // Angka yang didapat tampilkan pada TextView
        // Method ini dipanggil dari objek AsyncTask onProgressUpdate
        // objek AsyncTask bisa memanggil method ini dari Interface RandomInterface
        // yang diberikan pada saat objek AsyncTask diinisialisasi
//        textSecond.setText(angka.toString());
//        textHour.setText(angka.toString());
//        textMinute.setText(angka.toString());
    }
}
