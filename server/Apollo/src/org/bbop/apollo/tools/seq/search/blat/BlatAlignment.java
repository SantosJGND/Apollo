package org.bbop.apollo.tools.seq.search.blat;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.Collection;

import org.bbop.apollo.tools.seq.search.Alignment;
import org.gmod.gbol.bioObject.Match;
import org.gmod.gbol.bioObject.Region;
import org.gmod.gbol.bioObject.conf.BioObjectConfiguration;
import org.gmod.gbol.simpleObject.AnalysisFeature;
import org.gmod.gbol.simpleObject.Feature;

public class BlatAlignment implements Alignment {

	private int numMatches;
	private int numMismatches;
	private int numRepmatches;
	private int qNumInsert;
	private int tNumInsert;
	private String strand;
	private String qName;
	private int qSize; 
	private int qStart;
	private int qEnd;
	private String tName;
	private int tSize;
	private int tStart;
	private int tEnd;
	private int blockCount;
	private int[] blockSizes;
	private int[] qStarts;
	private int[] tStarts;

	public BlatAlignment(int numMatches, int numMismatches, int numRepmatches, int qNumInsert, int tNumInsert, String strand, 
			String qName, int qSize, int qStart, int qEnd, String tName, int tSize, int tStart, int tEnd, 
			int blockCount,	int[] blockSizes, int[] qStarts, int[] tStarts) {
		init(numMatches, numMismatches, numRepmatches, qNumInsert, tNumInsert, strand, 
				qName, qSize, qStart, qEnd, tName, tSize, tStart, tEnd, 
				blockCount,	blockSizes, qStarts, tStarts);
	}
	
	protected void init(int numMatches, int numMismatches, int numRepmatches, int qNumInsert, int tNumInsert, String strand, 
			String qName, int qSize, int qStart, int qEnd, String tName, int tSize, int tStart, int tEnd, 
			int blockCount,	int[] blockSizes, int[] qStarts, int[] tStarts) {
		this.numMatches = numMatches;
		this.numMismatches = numMismatches;
		this.numRepmatches = numRepmatches;
		this.qNumInsert = qNumInsert;
		this.tNumInsert = tNumInsert;
		this.strand = strand;
		this.qName = qName;
		this.qSize = qSize; 
		this.qStart = qStart;
		this.qEnd = qEnd;
		this.tName = tName;
		this.tSize = tSize;
		this.tStart = tStart;
		this.tEnd = tEnd;
		this.blockCount = blockCount;
		this.blockSizes = blockSizes;
		this.qStarts = qStarts;
		this.tStarts = tStarts;
	}

	public String getQueryId() {
		return qName;
	}

	public String getSubjectId() {
		return tName;
	}

	public int getQueryStart() {
		return qStart;
	}

	public int getQueryEnd() {
		return qEnd;
	}
	
	public int getSubjectStart() {
		return tStart;
	}

	public int getSubjectEnd() {
		return tEnd;
	}
	
	public int getQuerySpan() {
		return qEnd - qStart;
	}
	
	public int getSubjectSpan() {
		return tEnd - tStart;
	}
	
	public String getStrand() {
		return strand;
	}
	
	public double getScore() {
		int sizeMult = sizeMult();
		return sizeMult * (numMatches + (numRepmatches >> 1)) -
				sizeMult * numMismatches - qNumInsert - tNumInsert;
	}
	
	public double getPercentId(boolean ismRNA) {
		return 100 - milliBad(ismRNA);
	}
	
	private boolean isProtein() {
		int last = blockCount - 1;
		return ( ((tEnd == tStarts[last] + 3 * blockSizes[last]) && getStrand().equals("+")) ||
        ((tStart == tSize - (tStarts[last] + 3 * blockSizes[last]) && getStrand().equals("-"))) );
	}
	
	private int sizeMult() {
		if (isProtein()) {
			return 3;
		} else {
			return 1;
		}
	}
	
	private double milliBad(boolean ismRNA) {
		int sizeMult = sizeMult();
		
        int qAlnSize = getQuerySpan() * sizeMult;
        int tAlnSize = getSubjectSpan();
        int alnSize = min(qAlnSize, tAlnSize);
        if (alnSize <= 0) {
            return 0;
        }

        int sizeDiff = qAlnSize - tAlnSize;
        if (sizeDiff < 0) {
        	if (ismRNA) {
        		sizeDiff = 0;
        	} else {
        		sizeDiff = -sizeDiff;
        	}
        }

        int insertFactor = qNumInsert;
        if (!ismRNA) {
            insertFactor += tNumInsert;
        }

        int total = (numMatches + numRepmatches + numMismatches) * sizeMult;

        if (total != 0) {
        	return (1000 * (numMismatches * sizeMult + insertFactor + 
        			round(3 * log(1 + sizeDiff)))) / total;
        } else {
        	return 0;
        }
	}
	
	public Match convertToMatch(BioObjectConfiguration conf) {
		AnalysisFeature analysisFeature = new AnalysisFeature();
		Match match = new Match(null, null, false, null, analysisFeature, conf);
		analysisFeature.setRawScore(getScore());
		//analysisFeature.setSignificance(getEValue());
		Feature query = new Feature();
		query.setType(conf.getDefaultCVTermForClass("Region"));
		query.setUniqueName(getQueryId());
		int queryFmin = getQueryStart() + 1;
		int queryFmax = getQueryEnd();
		int queryStrand = 1;
		if (getStrand().equals("-")) {
			queryStrand = -1;
		}
		//--queryFmin;
		match.setQueryFeatureLocation(queryFmin, queryFmax, queryStrand, new Region(query, conf));
		Feature subject = new Feature();
		subject.setType(conf.getDefaultCVTermForClass("Region"));
		subject.setUniqueName(getSubjectId());
		int subjectFmin = getSubjectStart() + 1;
		int subjectFmax = getSubjectEnd();
		int subjectStrand = 1;
		if (getStrand().equals("-")) {
			subjectStrand = -1;
		}
		//--subjectFmin;
		match.setSubjectFeatureLocation(subjectFmin, subjectFmax, subjectStrand, new Region(subject, conf));
		match.setIdentity(getPercentId(true));
		return match;
	}
	
	public Collection<Match> convertToMatches(BioObjectConfiguration conf) {
		Collection<Match> matches = new ArrayList<Match>();
		Match match = convertToMatch(conf);
		matches.add(match);
		
        int nparts = qStarts.length;
        for ( int n = 0; n < nparts; n++ ) {
        	int blkSize = blockSizes[n];
        	
        	int ptStart = tStarts[n] + 1;    	
        	int ptEnd = ptStart + blkSize - 1;
        	
        	int pqStart = qStarts[n] + 1;
        	int pqEnd = pqStart + blkSize - 1;
        	
        	int pStrand = 1;
        	if (getStrand().equals("-")) {
        		pqStart = qSize - (qStarts[n] + blkSize) + 1;
        		pqEnd = qSize - qStarts[n];
        		pStrand = -1;
        	}
        	
        	Match pm = new Match(null, null, false, null, null, conf);
        	
        	Feature pqf = new Feature();
        	pqf.setType(conf.getDefaultCVTermForClass("Region"));
        	pqf.setUniqueName(getQueryId());
        	pm.setQueryFeatureLocation(pqStart, pqEnd, pStrand, new Region(pqf, conf));
        	
        	Feature ptf = new Feature();
        	ptf.setType(conf.getDefaultCVTermForClass("Region"));
        	ptf.setUniqueName(getSubjectId());
        	pm.setSubjectFeatureLocation(ptStart, ptEnd, pStrand, new Region(ptf, conf));
        	
        	matches.add(pm);
        }
		return matches;
	}

	protected BlatAlignment() {
	}
	
}
