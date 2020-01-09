package com.example.wsbp.ajax;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.wicketstuff.annotation.mount.MountPath;

import com.example.wsbp.page.SignPage;


@MountPath("Ajax")
public class AjaxPage extends WebPage {

	private static final long serialVersionUID = -2232572890294979377L;

	public AjaxPage(){

        var container = new WebMarkupContainer("container") {
            private static final long serialVersionUID = -6574758745521291898L;

            @Override
            protected void onInitialize() {
                super.onInitialize();
                // Ajaxの更新対象となるコンポーネントには setOutputMarkupId(true) を実行する
                // これにより、JavaScriptが処理するための id がタグに加えられる
                setOutputMarkupId(true);
                // 非表示（visible = false）状態から表示（visible = true）になる可能性があるタグには、
                // setOutputMarkupPlaceholderTag(true) を実行する。これにより、表示上は消えても、JavaScriptが
                // 処理できるタグが残る
                setOutputMarkupPlaceholderTag(true);
                setVisible(true);
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();
                // コンポーネントがAjaxなどで更新される度に実行されるメソッド
                // ここでは、コンポーネントの表示/非表示状態を切り替えている
                setVisible(!isVisibleInHierarchy());
            }
        };
        add(container);

        var label = new Label("label", Model.of("こんにちは"));
        //WebMarkupContainerにadd
        container.add(label);

        var link = new Link<>("link"){
            private static final long serialVersionUID = 7390925389581043480L;

            @Override
            public void onClick() {

                setResponsePage(new SignPage());
            }
        };
        //WebMarkupContainerにadd
        container.add(link);

        var ajaxLink = new AjaxLink<>("link") {
			private static final long serialVersionUID = -2936210058925141814L;

			@Override
            public void onClick(AjaxRequestTarget target) {

                // AjaxLinkがクリックされた時の処理
                target.add(container);
            }
        };
        add(ajaxLink);


    }

}
