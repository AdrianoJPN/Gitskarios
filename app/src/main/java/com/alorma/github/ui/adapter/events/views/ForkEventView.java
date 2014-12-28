package com.alorma.github.ui.adapter.events.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.alorma.github.R;
import com.alorma.github.sdk.bean.dto.response.GithubEvent;
import com.alorma.github.sdk.bean.dto.response.events.payload.ForkEventPayload;
import com.alorma.githubicons.GithubIconDrawable;
import com.alorma.githubicons.GithubIconify;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Bernat on 04/10/2014.
 */
public class ForkEventView extends GithubEventView<ForkEventPayload> {
	public ForkEventView(Context context) {
		super(context);
	}

	public ForkEventView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ForkEventView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void inflate() {
		inflate(getContext(), R.layout.payload_watch, this);
	}

	@Override
	protected void populateView(GithubEvent event) {
		TextView actionType = (TextView) findViewById(R.id.actionType);
		actionType.setText(R.string.forked);

		ImageView authorAvatar = (ImageView) findViewById(R.id.authorAvatar);

		ImageLoader.getInstance().displayImage(event.actor.avatar_url, authorAvatar);

		TextView authorName = (TextView) findViewById(R.id.authorName);
		authorName.setText(event.actor.login);

		ImageView actionImage = (ImageView) findViewById(R.id.actionImage);
		Drawable drawable = new GithubIconDrawable(getContext(), GithubIconify.IconValue.octicon_repo_forked).colorRes(R.color.icons);

		if (drawable != null) {
			actionImage.setImageDrawable(drawable);
			TextView action = (TextView) findViewById(R.id.action);
			action.setText(event.repo.name);
		}
	}

	@Override
	protected ForkEventPayload convert(Gson gson, String s) {
		return gson.fromJson(s, ForkEventPayload.class);
	}
}
