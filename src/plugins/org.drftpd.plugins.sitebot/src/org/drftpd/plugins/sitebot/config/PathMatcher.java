/*
 * This file is part of DrFTPD, Distributed FTP Daemon.
 *
 * DrFTPD is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * DrFTPD is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DrFTPD; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.drftpd.plugins.sitebot.config;

import org.apache.oro.text.GlobCompiler;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Perl5Matcher;
import org.drftpd.vfs.DirectoryHandle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author djb61
 * @version $Id$
 */
public class PathMatcher {

	private org.apache.oro.text.regex.Pattern _globPat;
	private Pattern _regexPat;

	private boolean _regex;

	private String _pathPattern;

	public PathMatcher(String pathPattern, boolean regex) throws MalformedPatternException {
		if (regex) {
			_regexPat = Pattern.compile(pathPattern, Pattern.CASE_INSENSITIVE);
		} else {
			_globPat = new GlobCompiler().compile(pathPattern);
		}
		_regex = regex;
		_pathPattern = pathPattern;
	}

	public boolean checkPath(DirectoryHandle file) {
		String path = file.getPath().concat("/");

		if (_regex) {
			Matcher m = _regexPat.matcher(path);
			return m.matches();
		} else {
			Perl5Matcher m = new Perl5Matcher();
			return m.matches(path, _globPat);
		}
	}

	public String getPathSuffix() {
		int index = _pathPattern.lastIndexOf('/');
		return _pathPattern.substring(0,index);
	}

	public String getRelativePath(DirectoryHandle file) {
		if (_regex) {
			String path = file.getPath().concat("/");
			Matcher m = _regexPat.matcher(path);
			if (m.group("announce") != null) {
				return m.group("announce");
			}
			else if (m.group("truncate") != null) {
				return file.getPath().substring(m.group("truncate").length());
			}
			return path;
		} else {
			return file.getPath().substring(getPathSuffix().length()+1);
		}
	}
}
