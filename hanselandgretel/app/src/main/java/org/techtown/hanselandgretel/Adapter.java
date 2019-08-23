package org.techtown.hanselandgretel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.techtown.hanselandgretel.QuizActivity;
import androidx.viewpager.widget.PagerAdapter;


/**
 * Created by HAN on 2018. 1. 8..
 */

// extends PagerAdapter 를 하고 나면 빨간줄이 그어지는데 반드시 상속해야할 메소드를 상속하지 않았기 때문.
// 해당 메소드들을 상속해준다
public class Adapter extends PagerAdapter {


    int stageLevel = ((QuizActivity)QuizActivity.context).stagelevel;


    // R.drawable.(사진파일이름)으로 images 배열 생성
    private int[] images0 = {R.drawable.c0, R.drawable.c0, R.drawable.q0};
    private int[] images1 = {R.drawable.c1, R.drawable.c1, R.drawable.q1};
    private int[] images2 = {R.drawable.c2, R.drawable.c2, R.drawable.q2};
    private int[] images3 = {R.drawable.c3, R.drawable.c3, R.drawable.q3};
    private int[] images4 = {R.drawable.c4, R.drawable.c4, R.drawable.q4};
    private String[] story0 = {" 냠냠. 쿠키가 참 맛있다 . 왜 쳐다보는거야? 이게 필요해? ", "아...쿠키를 모아야 한다구? 그럼 내가 최근의 본 영화 제목을 맞춰봐!",
            "위의 그림을 보면 충분히 맞출 수 있을거야!"};
    private String[] story1 = {" 안녕? 난 월드 스타 BTS야 무슨일이니? 뭐 쿠키가 필요하다구?", " 흠.. 그냥 주긴 그런데 다음 의미가 있는 카드들로 " +
            "하나의 단어를 맞춰봐! ", " \'간단하게\' 생각하는게 좋을껄?"};
    private String[] story2 = {" 어이 어이, 무슨일이야! 난 마법천자문의 손오공이라구~!\n한자를 아는건 기본 지식이지. 그렇게 생각하지 않니?  ",
            "내 나이에 대한 힌트를 줄테니 한번 맞춰봐~", " 한자를 모르는건 아니겠지? \n 잘생각해 보라구~"};
    private String[] story3 = {" 안녕하세요. 나천잽니다. Genius요.  뭘 찾고 계시다구요? \n  쿠키 말씀이신가요? 제가 보긴 했습니다. ",
            "쿠키엔 마력이 깃들어있어요. 위험해요. Dangerous요\n하지만, 마녀의 퀴즈를 풀면 마법을 풀 수 있을것 같아요",
            "흠...제가 보기에도 어렵군요.. 힘내세요. \nFighting이요."};
    private String[] story4 = {" 으아아아아아;;;; \n 깜짝이야. 왜이렇게 화내는 거야아;;;;; ",
            "나는 쿠기가 있길래 먹으려고 했을 뿐이라구..;;;;;\n 뭐어어어어?? 위험한 쿠키라구??\n 그치마안...나도 배가 고프단 마리야...",
            "다른 먹을 껄 준다구?? 알았어.. 그나저나 UniqueGood팀에서 \n 메세지가 왔어..~ 암호화 돼있는거 같은데 이걸 풀어주면\n" +
                    "쿠키를 너에게 줄게에"};
    private LayoutInflater inflater;
    private Context context;


    // 해당 context가 자신의 context 객체와 똑같이 되도록 생성자를 만듬
    public Adapter(Context context){
        this.context = context;
    }
    @Override
    public int getCount() {
        return images0.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        // object를 LinearLayout 형태로 형변환했을 때 view와 같은지 여부를 반환
//        return view == ((LinearLayout)object); 으로 했을때 오류 나서 View 로 바꿈..
        return view == ((View)object);
    }

    // 각각의 item을 인스턴스 화
    @Override
    public Object instantiateItem( ViewGroup container, int position) {
        //초기화

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_quiz, container, false);
        ImageView qimg = (ImageView)v.findViewById(R.id.qimg);
        TextView story = (TextView)v.findViewById(R.id.story);

        Log.d("stagelevel2 :", ""+stageLevel);

        switch(stageLevel) {
            case 0 : qimg.setImageResource(images0[position]);
                story.setText(story0[position]);
                break;
            case 1 : qimg.setImageResource(images1[position]);
                story.setText(story1[position]);
                break;
            case 2 : qimg.setImageResource(images2[position]);
                story.setText(story2[position]);
                break;
            case 3 : qimg.setImageResource(images3[position]);
                story.setText(story3[position]);
                break;
            case 4 : qimg.setImageResource(images4[position]);
                story.setText(story4[position]);
                break;

        }

        container.addView(v);
        return v;
    }

    //할당을 해제
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.invalidate();
//        super.destroyItem(container, position, object);
    }
}
