package org.jabox.webapp.panels;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.persistence.provider.GeneralDao;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jabox.apis.Connector;
import org.jabox.apis.ConnectorConfig;
import org.jabox.apis.Manager;
import org.jabox.model.DefaultConfiguration;

public class HeaderLinksPanel extends Panel {

	protected static final int ALM = 0;

	protected static final int ITS = 1;

	protected static final int CIS = 2;

	protected static final int RMS = 3;

	@SpringBean
	protected GeneralDao _generalDao;

	@SpringBean
	protected Manager<Connector> _manager;

	public HeaderLinksPanel(String id, int selected) {
		super(id);
		final DefaultConfiguration dc = _generalDao.getDefaultConfiguration();

		List<Tab> tabs = new ArrayList<Tab>();
		tabs.add(new Tab("A.L.M. (Jabox)", "/web/ManageServers",
				selected == ALM));

		if (dc.getIts() != null) {
			tabs.add(new Tab(getTabName("I.T.S.", dc.getIts()), "/web/ItsPage",
					dc.getIts().getServer().getUrl(), selected == ITS));
		}

		if (dc.getCis() != null) {
			tabs.add(new Tab(getTabName("C.I.S.", dc.getCis()), "/web/CisPage",
					dc.getCis().getServer().getUrl(), selected == CIS));
		}

		if (dc.getRms() != null) {
			tabs.add(new Tab(getTabName("R.M.S.", dc.getRms()), "/web/RmsPage",
					dc.getRms().getServer().getUrl(), selected == RMS));
		}

		add(new TabsList("tabs", tabs));
	}

	/**
	 * 
	 * @param prefix
	 *            The prefix of the name
	 * @param cc
	 *            The ConnectorConfig
	 * @return
	 */
	private String getTabName(String prefix, ConnectorConfig cc) {
		Connector ci = _manager.getConnectorInstance(cc);
		return prefix + "(" + ci.getName() + ")";
	}

	private static final long serialVersionUID = 9038957597332426470L;
}