/*
 * Copyright (C) 2008 Andrea Zito
 * 
 * This file is part of jMmsLib.
 *
 * jMmsLib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of 
 * the License, or  (at your option) any later version.
 *
 * jMmsLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with jMmsLib.  If not, see <http://www.gnu.org/licenses/>.
 */ 
//package net.sourceforge.jmmslib;

/**
 * An error occur reading the content of a mms part.
 * @author Andrea Zito
 *
 */
public class MmsContentException extends Exception {

	private static final long serialVersionUID = 1350614339552589552L;

	public MmsContentException(String description) {
		super(description);
	}

}
