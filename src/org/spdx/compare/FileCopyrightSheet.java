/**
 * Copyright (c) 2015 Source Auditor Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
*/
package org.spdx.compare;

import org.apache.poi.ss.usermodel.Workbook;
import org.spdx.rdfparser.model.SpdxFile;

import com.google.common.base.Objects;

/**
 * Sheet comparing file copyrights
 * @author Gary O'Neall
 *
 */
public class FileCopyrightSheet extends AbstractFileCompareSheet {
	
	static final int COPYRIGHT_COL_WIDTH = 60;
	
	public FileCopyrightSheet(Workbook workbook, String sheetName) {
		super(workbook, sheetName);
	}

	static void create(Workbook wb, String sheetName) {
		AbstractFileCompareSheet.create(wb, sheetName, COPYRIGHT_COL_WIDTH);
	}

	/* (non-Javadoc)
	 * @see org.spdx.compare.AbstractFileCompareSheet#valuesMatch(org.spdx.compare.SpdxComparer, org.spdx.rdfparser.model.SpdxFile, int, org.spdx.rdfparser.model.SpdxFile, int)
	 */
	@Override
	boolean valuesMatch(SpdxComparer comparer, SpdxFile fileA, int docIndexA,
			SpdxFile fileB, int docIndexB) throws SpdxCompareException {
        return Objects.equal(fileA.getCopyrightText(),
				fileB.getCopyrightText());
	}

	/* (non-Javadoc)
	 * @see org.spdx.compare.AbstractFileCompareSheet#getFileValue(org.spdx.rdfparser.model.SpdxFile)
	 */
	@Override
	String getFileValue(SpdxFile spdxFile) {
		String retval = spdxFile.getCopyrightText();
		if (retval == null) {
			retval = "NONE";
		}
		return retval;
	}

}
