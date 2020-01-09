package com.example.wsbp.page;

import com.example.wsbp.MySession;
import com.example.wsbp.page.signed.SignedPage;
import com.example.wsbp.service.IUserService;
import com.giffing.wicket.spring.boot.context.scan.WicketSignInPage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.validator.StringValidator;
import org.wicketstuff.annotation.mount.MountPath;

@WicketSignInPage
@MountPath("SignEx")
public class SignExPage extends WebPage {

  @SpringBean
  private IUserService service;

  public SignExPage() {

    var signInContainer = new WebMarkupContainer("signInContainer") {

      @Override
      protected void onInitialize() {
        super.onInitialize();
        setOutputMarkupId(true);
      }

      @Override
      protected void onConfigure() {
        super.onConfigure();
        setVisible(MySession.get().getUserName() == null);
      }
    };
    add(signInContainer);

    var signInLabel = new Label("signInLabel", Model.of("ログインしてください"));
    signInContainer.add(signInLabel);

    var userNameModel = Model.of("");
    var userPassModel = Model.of("");

    var userInfoForm = new Form<>("userInfo") {
      @Override
      protected void onSubmit() {
        var userName = userNameModel.getObject();
        var userPass = userPassModel.getObject();
        // b1992490...の定数で照合していたものを、DB経由に変更
        if (service.existsUser(userName, userPass)) {
          MySession.get().sign(userName);
        }
        setResponsePage(new SignedPage());
      }
    };
    signInContainer.add(userInfoForm);

    var userNameField = new TextField<>("userName", userNameModel) {
      @Override
      protected void onInitialize() {
        super.onInitialize();
        // 文字列の長さを8〜32文字に制限するバリデータ
        add(StringValidator.lengthBetween(8, 32));
      }
    };

    userInfoForm.add(userNameField);

    var userPassField = new PasswordTextField("userPass", userPassModel) {
      @Override
      protected void onInitialize() {
        super.onInitialize();
        // 文字列の長さを8〜32文字に制限するバリデータ
        add(StringValidator.lengthBetween(8, 32));
      }
    };
    userInfoForm.add(userPassField);


    var signedContainer = new WebMarkupContainer("signedContainer") {

      @Override
      protected void onInitialize() {
        super.onInitialize();
        setOutputMarkupId(true);
      }

      @Override
      protected void onConfigure() {
        super.onConfigure();
        setVisible(!(MySession.get().getUserName() == null));
      }
    };
    add(signedContainer);

    var signedLabel = new Label("signedLabel", Model.of("ログイン済みです"));
    signedContainer.add(signedLabel);

    var signedLink = new Link<>("signedLink"){

      @Override
      public void onClick() {
        setResponsePage(new SignedPage());
      }
    };
    signedContainer.add(signedLink);

    AjaxSelfUpdatingTimerBehavior behavior = new AjaxSelfUpdatingTimerBehavior(Duration.seconds(20)) {
      @Override
      protected void onPostProcessTarget(AjaxRequestTarget target) {
        super.onPostProcessTarget(target);
        target.add(~~~Container);
        target.add(~~~Container);
      }
    };
    add(behavior);


  }

}
