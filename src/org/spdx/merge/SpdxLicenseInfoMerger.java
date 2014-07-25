/**
 * Copyright (c) 2014 Gang Ling.
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
package org.spdx.merge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.spdx.compare.LicenseCompareHelper;
import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.SPDXDocument;
import org.spdx.rdfparser.SPDXNonStandardLicense;

/**
 * @author Gang Ling
 *
 */
public class SpdxLicenseInfoMerger {

	public Collection<SPDXNonStandardLicense> mergeNonStandardLic(SPDXDocument[] mergeDocs,
					HashMap<SPDXDocument,HashMap<String,String>> licIdMap) throws InvalidSPDXAnalysisException{
		
		//an array to hold the final result 
		ArrayList<SPDXNonStandardLicense> licInfoResult = new ArrayList<SPDXNonStandardLicense>();
		//an array to hold the non-standard license info from master SPDX document
		SPDXNonStandardLicense[] MasterNonStandardLicInfo = mergeDocs[0].getExtractedLicenseInfos();
		//an index number to track license id
		int licId = 0;
		//first, add master's non-standard license into the final result array
		for(int q = 0; q < MasterNonStandardLicInfo.length; q++){
			SPDXNonStandardLicense temp = MasterNonStandardLicInfo[q];
			licInfoResult.add(temp);
			licId +=1;
			temp = null;
		}
		//compare and merge non-standard license info
		for(int i = 1; i < mergeDocs.length; i++){
			//an array to hold non-standard license info from current child SPDX document
			SPDXNonStandardLicense[] childNonStandardLicInfo = mergeDocs[i].getExtractedLicenseInfos();
			//an HashMap to track the changing of license ID from current child SPDX document
			HashMap<String, String> idMap = new HashMap<String, String>();
			
	        for(int k = 0; k < licInfoResult.size(); k++){
	        	boolean foundTextMatch = false;
	        	for(int p = 0; p < childNonStandardLicInfo.length; p++){
	        		if(LicenseCompareHelper.isLicenseTextEquivalent(licInfoResult.get(k).getText(), childNonStandardLicInfo[p].getText())){
	        			foundTextMatch = true;
	           		}
	        		if(!foundTextMatch){
	        			String oldLicId = childNonStandardLicInfo[p].getId();
	        			licId +=1;
	        			String newLicId = "LicenseRef-" + licId;	       
	        			SPDXNonStandardLicense clonedLicInfo = childNonStandardLicInfo[p].clone();//need work on the clone object
	        			//over-write the license ID
	        			clonedLicInfo.setId(newLicId);
	        			idMap.putIfAbsent(oldLicId, newLicId);
	        			licInfoResult.add(clonedLicInfo);
	        			newLicId = null;
	        			oldLicId = null;
	        		}
	        	}	      
	        }
	        licIdMap.put(mergeDocs[i], idMap);			
		}
		return licInfoResult;
	}
}
