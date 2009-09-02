package org.jabox.webapp.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.jabox.environment.Environment;
import org.jabox.webapp.borders.NavomaticBorder;

/**
 * Homepage
 */
public class HomePage extends WebPage {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	public HomePage(final PageParameters parameters) {
		NavomaticBorder navomaticBorder = new NavomaticBorder("navomaticBorder");
		add(navomaticBorder);

		System.out.println("user.home: " + Environment.getBaseDir());
		navomaticBorder.add(new BookmarkablePageLink("login", MainMenu.class, parameters));

		addAjaxCounter(navomaticBorder);
	}

	private void addAjaxCounter(Border border) {
		Model model = new Model() {
			private int counter = 0;

			public Object getObject() {
				return new Integer(counter++);
			}
		};
		final Label label = new Label("counter", model);
		label.setOutputMarkupId(true);

		border.add(new AjaxFallbackLink("link") {
			public void onClick(AjaxRequestTarget target) {
				if (target != null) {
					// target is only available in an ajax request
					target.addComponent(label);
				}
			}
		});
		border.add(label);
	}

}