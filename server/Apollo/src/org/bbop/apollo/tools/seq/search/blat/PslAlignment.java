package org.bbop.apollo.tools.seq.search.blat;

import org.bbop.apollo.tools.seq.search.AlignmentParsingException;

public class PslAlignment extends BlatAlignment {

	private final static int EXPECTED_NUM_FIELDS = 21;
	private final static String DELIMITER = "\t";
	
	public PslAlignment(String tabbedAlignment, int index) throws AlignmentParsingException {
		String []fields = tabbedAlignment.split(DELIMITER);
		if (fields.length != EXPECTED_NUM_FIELDS) {
			throw new AlignmentParsingException("Incorrect number of fields: found " + fields.length + " but expected " + EXPECTED_NUM_FIELDS);
		}
        int numMatches = Integer.parseInt(fields[0]);
        int numMismatches = Integer.parseInt(fields[1]);
        int numRepmatches = Integer.parseInt(fields[2]);
        int nCount = Integer.parseInt(fields[3]);
        int qNumInsert = Integer.parseInt(fields[4]);
        int qBaseInsert = Integer.parseInt(fields[5]);
        int tNumInsert = Integer.parseInt(fields[6]);
        int tBaseInsert = Integer.parseInt(fields[7]);
        String strand = fields[8];
        String qName = fields[9] + ".match." + index;
        int qSize = Integer.parseInt(fields[10]);
        int qStart = Integer.parseInt(fields[11]);
        int qEnd = Integer.parseInt(fields[12]);
        String tName = fields[13];
        int tSize = Integer.parseInt(fields[14]);
        int tStart = Integer.parseInt(fields[15]);
        int tEnd = Integer.parseInt(fields[16]);
        int blockCount = Integer.parseInt(fields[17]);
        String[] blkSizes = fields[18].split(",");
        String[] qryBlkStarts = fields[19].split(",");
        String[] tgtBlkStarts = fields[20].split(",");
        
        int[] blockSizes = new int[blockCount];
        int[] qStarts = new int[blockCount];
        int[] tStarts = new int[blockCount];
        for ( int i = 0; i < blockCount; i++ ) {
        	blockSizes[i] = Integer.parseInt(blkSizes[i]);
        	qStarts[i] = Integer.parseInt(qryBlkStarts[i]);
        	tStarts[i] = Integer.parseInt(tgtBlkStarts[i]);
        }
        
		init(numMatches, numMismatches, numRepmatches, qNumInsert, tNumInsert, strand, 
				qName, qSize, qStart, qEnd, tName, tSize, tStart, tEnd, 
				blockCount,	blockSizes, qStarts, tStarts);
	}
}
