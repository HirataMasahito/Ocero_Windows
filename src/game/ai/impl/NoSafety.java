package game.ai.impl;

import game.ai.AiBase;
import game.ai.impl.NoSafety.Util.AroundPos;
import game.othello.Bord;

import java.util.ArrayList;
import java.util.List;

import common.Common;
import common.Common.Stone;
import common.Pos;


public class NoSafety extends AiBase {
	public static class Util {
		public static void GetSafePosList(Bord bord, Stone color, ArrayList<Pos> safetyList, ArrayList<Pos> safePosList){
			ArrayList<Pos> safePosListWork = new ArrayList<Pos>();
			for (Pos pos : safetyList) {
				if(bord.getColor(pos) == color) safePosListWork.add(pos);
			}

			if (!safePosListWork.isEmpty()){
				for (Pos pos : safePosListWork) {
					safetyList.remove(pos);
					safePosList.add(pos);
					
					AroundPos aPos = new AroundPos(pos);
					for (Pos safetyPosWork : aPos.GetList()) {
						if (bord.getColor(safetyPosWork) == Stone.NONE && isSafety(safetyPosWork, safePosList)) safetyList.add(safetyPosWork);
					}
				}
				GetSafePosList(bord, color, safetyList, safePosList);
			}
		}

		private static boolean isSafety(Pos pos, ArrayList<Pos> safePosList){
			AroundPos aPos = new AroundPos(pos);
			Pos[][] poss = new Pos[][]{{aPos.posLu, aPos.posRd},{aPos.posCu,aPos.posCd},{aPos.posRu, aPos.posLd},{aPos.posLc, aPos.posRc}};
			
			boolean isSafety = true;
			for (Pos[] possWork : poss) {
				boolean flg = false;
				for (Pos posWork : possWork) {
					if (!isPosExists(posWork) || safePosList.contains(posWork)) flg = true;
				}
				if(!flg){
					isSafety = false;
					break;
				}
			}
			
			return isSafety;
		}
		
		private static boolean isPosExists(Pos pos) {
			return pos.getX() >= Common.X_MIN_LEN && pos.getX() < Common.X_MAX_LEN && pos.getY() >= Common.Y_MIN_LEN && pos.getY() < Common.Y_MAX_LEN;
		}
		
		public static ArrayList<Pos> InitialSafetyList(){
			ArrayList<Pos> safetyList = new ArrayList<Pos>();
			safetyList.add(new Pos(0, 0));
			safetyList.add(new Pos(0, 7));
			safetyList.add(new Pos(7, 0));
			safetyList.add(new Pos(7, 7));
			return safetyList;
		}
		
		public static class AroundPos{
			private Pos posLu;
			private Pos posCu;
			private Pos posRu;
			private Pos posLc;
			private Pos posRc;
			private Pos posLd;
			private Pos posCd;
			private Pos posRd;
			
			private AroundPos(Pos pos){
				posLu = new Pos(pos.getX() -1, pos.getY() -1);
				posCu = new Pos(pos.getX(), pos.getY() -1);
				posRu = new Pos(pos.getX() +1, pos.getY() -1);
				posLc = new Pos(pos.getX() -1, pos.getY());
				posRc = new Pos(pos.getX() +1, pos.getY());
				posLd = new Pos(pos.getX() -1, pos.getY() +1);
				posCd = new Pos(pos.getX(), pos.getY() +1);
				posRd = new Pos(pos.getX() +1, pos.getY() +1);
			}
			
			private ArrayList<Pos> GetList(){
				ArrayList<Pos> aroundList = new ArrayList<Pos>();
				if (isPosExists(posLu)) aroundList.add(posLu);
				if (isPosExists(posCu)) aroundList.add(posCu);
				if (isPosExists(posRu)) aroundList.add(posRu);
				if (isPosExists(posLc)) aroundList.add(posLc);
				if (isPosExists(posRc)) aroundList.add(posRc);
				if (isPosExists(posLd)) aroundList.add(posLd);
				if (isPosExists(posCd)) aroundList.add(posCd);
				if (isPosExists(posRd)) aroundList.add(posRd);
				return aroundList;
			}
		}
	}


	private ArrayList<Pos> mySafetyList = Util.InitialSafetyList();
	private ArrayList<Pos> mySafePosList = new ArrayList<Pos>();

	private ArrayList<Pos> yourSafetyList = Util.InitialSafetyList();
	private ArrayList<Pos> yourSafePosList = new ArrayList<Pos>();
	
	public NoSafety(Stone MyColor) {
		super(MyColor);
	}

	@Override
	public Pos WhereSet(Bord bord, Pos clickPos) {
		ArrayList<Pos> canSetList = bord.getCanSetList(getMyColor());
		if (canSetList.size() == 0) return null;
		
		Util.GetSafePosList(bord, getMyColor(), mySafetyList, mySafePosList);
		Util.GetSafePosList(bord, Stone.reverseStone(getMyColor()), yourSafetyList, yourSafePosList);
		
		removeVeryBadPos(canSetList, bord);
		removeBadPos(canSetList);
		
		Pos ret = null;
		int maxSafePosCnt = mySafePosList.size();
		for (Pos pos : canSetList) {
			Bord vrBord = bord.clone();
			vrBord.DoSet(pos, getMyColor(), true);
			
			ArrayList<Pos> mySafetyListTmp = new ArrayList<Pos>();
			mySafetyListTmp.addAll(mySafetyList);
			ArrayList<Pos> mySafePosListTmp = new ArrayList<Pos>();
			mySafePosListTmp.addAll(mySafePosList);
			
			Util.GetSafePosList(vrBord, getMyColor(), mySafetyListTmp, mySafePosListTmp);
			if (mySafePosListTmp.size() > maxSafePosCnt) {
				maxSafePosCnt = mySafePosListTmp.size();
				ret = pos;
			}
		}
		
		if (ret != null) return ret;
		
		removeLittleBadPos(canSetList, bord);

		ArrayList<Pos> canSetListTmp = new ArrayList<Pos>();
		for (Pos goodPos : goodPosList(bord)) {
			if (canSetList.contains(goodPos)) canSetListTmp.add(goodPos);
		}
		
		if(!canSetListTmp.isEmpty()) canSetList = canSetListTmp;
		
		Integer minYourSet = null;
		int maxStoneCnt = 0;
		int myStoneCnt = bord.GetCount(getMyColor());
		for (Pos pos : canSetList) {
			Bord vrBord = bord.clone();
			vrBord.DoSet(pos, getMyColor(), true);
			int maxStoneCntTmp = vrBord.GetCount(getMyColor()) - myStoneCnt;
			if (maxStoneCntTmp > maxStoneCnt) maxStoneCnt = maxStoneCntTmp;
		}
		
		for (Pos pos : canSetList) {
			Bord vrBord = bord.clone();
			vrBord.DoSet(pos, getMyColor(), true);
			int yourSet = vrBord.getCanSetList(Stone.reverseStone(getMyColor())).size();
			int myStoneCntAfter = vrBord.GetCount(getMyColor())  - myStoneCnt;
						
			if(myStoneCntAfter > (maxStoneCnt - 2)){
				if(minYourSet == null || yourSet < minYourSet){
					minYourSet = yourSet;
					ret = pos;
				}
			}
		}
		
		return ret;
	}
	
	private void removeVeryBadPos(ArrayList<Pos> canSetList, Bord bord){
		ArrayList<Pos> badPosList = new ArrayList<Pos>();
		for (Pos myPos : canSetList) {
			Bord myBord = bord.clone();
			myBord.DoSet(myPos, getMyColor(), true);

			simulateYours(myBord, myPos, badPosList, yourSafetyList);
		}

		if(canSetList.size() > badPosList.size()) canSetList.removeAll(badPosList);
	}
	
	private void removeLittleBadPos(ArrayList<Pos> canSetList, Bord bord) {
		ArrayList<Pos> badPosList = new ArrayList<Pos>();
		for (Pos myPos : canSetList) {
			Bord myBord = bord.clone();
			myBord.DoSet(myPos, getMyColor(), true);
			simulateYours(myBord, myPos, badPosList, goodPosList(myBord));
		}
		
		if(canSetList.size() > badPosList.size()) canSetList.removeAll(badPosList);
	}
	
	private void removeBadPos(ArrayList<Pos> canSetList){
		ArrayList<Pos> badPosList = new ArrayList<Pos>();
		for (Pos yourSafetyPos : yourSafetyList) {
			AroundPos aPos = new AroundPos(yourSafetyPos);
			for (Pos pos : aPos.GetList()) {
				if (!badPosList.contains(pos) && canSetList.contains(pos)) badPosList.add(pos);
			}
			
		}

		if(canSetList.size() > badPosList.size()) canSetList.removeAll(badPosList);
	}
	
	private ArrayList<Pos> goodPosList(Bord bord) {
		ArrayList<Pos> cornerPosList = Util.InitialSafetyList();
		ArrayList<Pos> goodPosList = new ArrayList<Pos>();
		for (Pos cornerPos : cornerPosList) {
			if (bord.getColor(cornerPos) == Stone.NONE) {
				AroundPos aPos = new AroundPos(cornerPos);
				for (Pos aroundCornerPos : aPos.GetList()) {
					AroundPos aAroundCornerPos = new AroundPos(aroundCornerPos);
					List<Pos> aAroundCornerPosList = aAroundCornerPos.GetList();
					if(aAroundCornerPosList.size() == 5 || aAroundCornerPosList.size() == 8){
						for (Pos pos : aAroundCornerPosList) {
							if (!goodPosList.contains(pos)) goodPosList.add(pos);
						}
					}
				}
			}
		}
		
		return goodPosList;
	}
	
	private void simulateYours(Bord myBord, Pos myPos, ArrayList<Pos> badPosList, ArrayList<Pos> ngList){
		ArrayList<Pos> yourCanSetList = myBord.getCanSetList(Stone.reverseStone(getMyColor()));
		
		for (Pos yourPos : yourCanSetList) {
			if(ngList.contains(yourPos) && !badPosList.contains(yourPos)){
				badPosList.add(myPos);
				break;
			}
		}
	}
}
