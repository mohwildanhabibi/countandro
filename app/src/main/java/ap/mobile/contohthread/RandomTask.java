package ap.mobile.contohthread;

import android.os.AsyncTask;

import java.util.Random;

public class RandomTask extends AsyncTask<Void, Integer, Void> {

    private final RandomInterface fragment;

    public RandomTask(RandomInterface fragment) {
        this.fragment = fragment;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Random random = new Random();
        try {
            while(true) {
                Integer angka = random.nextInt(9);
                Thread.sleep(500);
                publishProgress(angka);
            }
        } catch (Exception ex) {}
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        fragment.onAngkaDidapat(values[0]);
    }
}
