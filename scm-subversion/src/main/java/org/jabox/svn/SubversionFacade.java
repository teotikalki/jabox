package org.jabox.svn;

import java.io.File;

import org.jabox.model.Project;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;

public class SubversionFacade {
	private final SVNClientManager _clientManager;

	public SubversionFacade() {
		_clientManager = SVNClientManager.newInstance();

		if (!SubversionRepository.isInitialized()) {
			SubversionRepository.initialize();
		}

		FSRepositoryFactory.setup();
	}

	/**
	 * Checks out the base-dir of the subversion
	 * 
	 * @param storePath
	 *            the path where to store the subversion base-dir.
	 * @throws SVNException
	 */
	public void checkoutBaseDir(File storePath) throws SVNException {

		SVNURL svnDir = SVNURL.fromFile(SubversionRepository
				.getSubversionBaseDir());
		_clientManager.createRepository(svnDir, true);
		_clientManager.getUpdateClient().doCheckout(svnDir, storePath,
				SVNRevision.UNDEFINED, SVNRevision.HEAD, false);
	}

	public void commitProject(Project project, File tmpDir) throws SVNException {
		// Add files (svn add)
		SVNWCClient wcClient = _clientManager.getWCClient();
		wcClient.doAdd(new File(tmpDir, project.getName()), false, false, true,
				true);

		// Commit files (svn commit)
		SVNCommitClient commitClient = _clientManager.getCommitClient();

		File[] paths = new File[1];
		paths[0] = new File(tmpDir, project.getName());
		// paths[1] = new File("tags");
		// paths[2] = new File("branches");

		setProjectProperties(project, tmpDir);

		commitClient.doCommit(paths, false, "[JABOX] Initial Commit", false,
				true);
	}

	/**
	 * Set the bugtraq and svn:ignore properties to the project.
	 * 
	 * @param project
	 * @param rootDir
	 * @throws SVNException
	 */
	private void setProjectProperties(Project project, File rootDir)
			throws SVNException {
		File moduleFile = new File(rootDir, File.separator + project.getName()
				+ File.separator + "trunk" + File.separator + project.getName());
		setModuleProperties(moduleFile);
	}

	/**
	 * Set the bugtraq and snv:ignore properties to the module directory.
	 * 
	 * @param dir
	 * @throws SVNException
	 */
	private void setModuleProperties(File dir) throws SVNException {
		SVNWCClient wc = _clientManager.getWCClient();
		wc.doSetProperty(dir, "bugtraq:url",
				"http://localhost/cgi-bin/bugzilla/show_bug.cgi?id=%BUGID%",
				false, false, null);

		wc.doSetProperty(dir, "bugtraq:number", "true", false, false, null);
		wc.doSetProperty(dir, "bugtraq:append", "false", false, false, null);

		wc.doSetProperty(dir, "bugtraq:message", "[ %BUGID% ]", false, false,
				null);
		wc.doSetProperty(dir, "bugtraq:label", "Issue:", false, false, null);
		wc.doSetProperty(dir, "bugtraq:url",
				"http://localhost/cgi-bin/bugzilla/show_bug.cgi?id=%BUGID%",
				false, false, null);
		wc.doSetProperty(dir, "bugtraq:warnifnoissue", "true", false, false,
				null);
		wc.doSetProperty(dir, "svn:ignore", "target", false, false, null);
	}
}