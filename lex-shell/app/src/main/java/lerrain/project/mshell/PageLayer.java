package lerrain.project.mshell;

import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class PageLayer extends Layer
{
	int topH = 52;

	RelativeLayout top;

	TopButton[] tpb = new TopButton[4];

	TextView title;

	boolean dragging = false;

	float dragX;

	public PageLayer(Main window)
	{
		super(window, 0);

		init();
	}

	private void init()
	{
		LayoutParams lp;
		
		top = new RelativeLayout(window);
		top.setBackgroundColor(0xFFFFFFFF);
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, Ui.dp(topH));
		this.addView(top, lp);

		tpb[0] = new TopButton(this, 0);
		tpb[0].setImage("back");
		tpb[0].setPadding(Ui.dp(7), Ui.dp(7), Ui.dp(7), Ui.dp(7));
		tpb[0].setVisibility(View.INVISIBLE);
		lp = new LayoutParams(Ui.dp(44), Ui.dp(44));
		lp.setMargins(Ui.dp(6), Ui.dp(2), 0, 0);
		lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		top.addView(tpb[0], lp);

		tpb[0].setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				adapter.back(null);
			}
		});

		title = new TextView(window);
		title.setGravity(Gravity.CENTER);
		title.setTextSize(18);
		title.setText("");
		title.setTextColor(0xFF000000);
		lp = new LayoutParams(Ui.dp(200), Ui.dp(topH - 4));
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		top.addView(title, lp);

		title.setOnLongClickListener(new OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				openUrl("MF.pressTitle()");
				return true;
			}
		});

		title.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				wv.reload();
			}
		});

		for (int i=1;i<=2;i++)
		{
			tpb[i] = new TopButton(this, i);
			tpb[i].setPadding(Ui.dp(7), Ui.dp(7), Ui.dp(7), Ui.dp(7));
			lp = new LayoutParams(Ui.dp(44), Ui.dp(44));
			lp.setMargins(0, Ui.dp(2), Ui.dp(44 * i - 38), 0);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			top.addView(tpb[i], lp);
		}

		WebView wv = super.initWebView();
		lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.setMargins(0, Ui.dp(topH), 0, 0);
		this.addView(wv, lp);
	}

	public void setBackButton(boolean b)
	{
		if (tpb[0] != null)
			tpb[0].setVisibility(b ? View.VISIBLE : View.INVISIBLE);
	}

	public void setTitle(String text)
	{
		title.setText(text);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_MOVE)
		{
			if (dragging)
			{
				if (event.getX() > dragX)
				{
					int alpha = (int)Math.abs((event.getX() - dragX) * 192 / Ui.width);

					this.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
					rl.setTranslationX(event.getX() - dragX);
				}
			}
		}
		else if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			dragging = true;
			dragX = event.getX();
		}
		else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			if (dragging)
			{
				dragging = false;

				if (event.getX() - dragX > Ui.width / 2)
				{
					this.getRoot().back(null);
				}
				else
				{
					int x = (int)rl.getTranslationX();
					play(x, 0, -x, 0, 1);
				}
			}
		}

		return false;
	}

	protected void playEnter()
	{
		play(Ui.width, 0, -Ui.width, 0, 1);
	}

	protected void playOut()
	{
		int x = (int)rl.getTranslationX();
		play(x, 0, Ui.width - x, 0, 2);
	}
}
