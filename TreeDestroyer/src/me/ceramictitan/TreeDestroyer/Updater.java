/*
 * Updater for Bukkit.
 *
 * This class provides the means to safetly and easily update a plugin, or check to see if it is updated using dev.bukkit.org
 */

package me.ceramictitan.TreeDestroyer;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Updater {

	private TreeDestroyer plugin;
	private URL filesFeed;

	private String version;
	private String link;

	public Updater(TreeDestroyer plugin, String url) {
		this.plugin = plugin;

		try {
			this.filesFeed = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public boolean updateNeeded() {
		try {
			InputStream input = this.filesFeed.openConnection()
					.getInputStream();
			Document document = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(input);

			Node lastestFile = document.getElementsByTagName("item").item(0);
			NodeList files = lastestFile.getChildNodes();

			this.version = files.item(1).getTextContent()
					.replaceAll("[[a-zA-Z] ]", "").replace("[]", "");
			this.link = files.item(3).getTextContent();

			if (!plugin.getDescription().getVersion().equals(this.version)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public String getVersion() {
		return this.version;
	}

	public String getLink() {
		return this.link;
	}

}
