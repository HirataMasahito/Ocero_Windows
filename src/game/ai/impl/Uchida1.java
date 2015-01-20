package game.ai.impl;

import game.ai.AiBase;
import game.othello.Bord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import common.Common.Stone;
import common.Pos;

public class Uchida1 extends AiBase {

	private static final int V_MAX = 30;

	private static final int MIN = 0;
	private static final int MAX = 7;

	private static final List<Pos> UGAY_LIST;
	private static final List<Pos> HAJIS;

	private static boolean _vMax;


	private static List<Pos> yametokeList = new ArrayList<Pos>();

	private static final boolean debug = true;

	static{

		List<Pos> list2 = new ArrayList<Pos>();
		list2.add(new Pos(0,1));
		list2.add(new Pos(1,0));
		list2.add(new Pos(1,1));

		list2.add(new Pos(6,0));
		list2.add(new Pos(6,1));
		list2.add(new Pos(7,1));

		list2.add(new Pos(0,6));
		list2.add(new Pos(1,6));
		list2.add(new Pos(1,7));

		list2.add(new Pos(6,6));
		list2.add(new Pos(7,6));
		list2.add(new Pos(6,7));

		list2.add(new Pos(0,2));
		list2.add(new Pos(2,0));

//		list2.add(new Pos(0,5));
//		list2.add(new Pos(2,7));
//
//		list2.add(new Pos(5,0));
//		list2.add(new Pos(7,2));
//
//		list2.add(new Pos(5,7));
//		list2.add(new Pos(7,5));
		UGAY_LIST = list2;

		List<Pos> list3 = new ArrayList<Pos>();
		for (int i = 0; i < 7; i++) {
			list3.add(new Pos(0,i));
			list3.add(new Pos(i,0));
			list3.add(new Pos(i,7));
			list3.add(new Pos(7,i));
		}
		HAJIS = list3;
	}

	public Uchida1(Stone MyColor) {
		super(MyColor);
	}

	@Override
	public Pos WhereSet(Bord bord, Pos clickPos) {
		int now = bord.GetCount(Stone.BLACK) + bord.GetCount(Stone.WHITE);
		_vMax = now > V_MAX;
		if(debug && _vMax ){
			System.out.println("V_MAX!!");
		}
		Pos result = null;
		yametokeList = new ArrayList<Pos>();
		List<Pos> list = bord.getCanSetList(getMyColor());
		result = haji(result,list);

		List<Pos> newList = getNextOsusumeList(list);
		int r = 0;
		while(result == null && r < 10){

			result = osusume(result,newList);

			List<CalcPos> calcList = changeCalcList(bord,newList);
			result = max(result,calcList);
			result = choimate(result,bord);

			if (result == null){
				newList = getNextOsusumeList(list);
			}
			r++;
		}

		// もうだめだ、おしまいだ
		if (result == null){
			for (Pos pos : list) {
				if(!UGAY_LIST.contains(pos)){
					result = pos;
					break;
				}
			}
			if(debug && result != null){
				System.out.println("miss1!!");
			}
		}
		// もうだめだ、おしまいだ
		if (result == null){
			result = bord.SearchMaxPos(getMyColor());
			if(debug){
				System.out.println("miss2!!");
			}
		}
		return result;
	}

	private Pos choimate(Pos result,Bord bord){
		boolean f = false;
		if (result != null) {
			Bord bordc = bord.clone();
			bordc.DoSet(result, getMyColor(), true);
			List<Pos> list = bordc.getCanSetList(Stone.reverseStone(getMyColor()));

			for (Pos pos : list) {
				Bord bord2 = bordc.clone();
				bord2.DoSet(pos, Stone.reverseStone(getMyColor()), true);
				List<Pos> list2 = bord2.getCanSetList(getMyColor());
				if (list2.isEmpty()) {
					yametokeList.add(result);
					f = true;
					if(debug){
						System.out.println("choimate1"  + ":" +  result.getX() + "" + result.getY() + ":" +  pos.getX() + "" + pos.getY());
					}
					break;
				}

				if(HAJIS.contains(result)){
					if (Stone.reverseStone(getMyColor()).equals(bord2.getColor(result))){
						yametokeList.add(result);
						f = true;
						if(debug){
							System.out.println("choimate2"  + ":" +  result.getX() + "" + result.getY() + ":" +  pos.getX() + "" + pos.getY());
						}
						break;
					}
				}
			}

		}
		if (f){
			return null;
		}else{
			return result;
		}
	}

	private Pos haji(Pos result,List<Pos> list){
		// 端とれわんわん
		if (result == null) {
			for (Pos pos : list) {
				int x = pos.getX();
				int y = pos.getY();

				if ((x == MIN || x == MAX) && (y == MIN || y == MAX)) {
					result = pos;
					break;
				}
			}
			if(debug && result != null){
				System.out.println("haji");
			}
		}
		return result;
	}

	private Pos osusume(Pos result, List<Pos> list) {
		if (result == null) {
			for (Pos pos : list) {
				int x = pos.getX();
				int y = pos.getY();
				if (x == MIN || x == MAX || y == MIN || y == MAX) {
					result = pos;
					break;
				}
			}
			if(debug && result != null){
				System.out.println("osusume");
			}
		}
		return result;
	}

	private List<Pos> getNextOsusumeList(List<Pos> list) {
		List<Pos> okList = new ArrayList<Pos>();
		for (Pos pos : list) {
			if (_vMax || !UGAY_LIST.contains(pos)){
				if(!yametokeList.contains(pos)){
					okList.add(pos);
				}
			}
		}
		if(debug && okList != null){
			System.out.println("getNextOsusumeList" + okList.size());
		}

		return okList;
	}

	private Pos min(Pos result,List<CalcPos> calcList){
		if (result == null) {
			// ちっこい順にソート
			Collections.sort(calcList, new Comparator<CalcPos>() {
				public int compare(CalcPos a, CalcPos b) {
					if (a == null)
						return -1;
					if (b == null)
						return 1;
					return Integer.compare(a.num, b.num);
				}
			});
			for (CalcPos calcPos : calcList) {
				result = calcPos.getPos();
				break;
			}
			if(debug && result != null){
				System.out.println("min");
			}
		}
		return result;
	}

	private Pos max(Pos result,List<CalcPos> calcList){
		if (result == null) {
			Collections.sort(calcList, new Comparator<CalcPos>() {
				public int compare(CalcPos a, CalcPos b) {
					if (a == null) return 1;
					if (b == null) return -1;
					return  Integer.compare(b.num, a.num);
				}
			});
			for (CalcPos calcPos : calcList) {
				result = calcPos.getPos();
				break;
			}
			if(debug && result != null){
				System.out.println("max");
			}
		}
		return result;
	}


	private List<CalcPos> changeCalcList(Bord bord, List<Pos> list) {
		List<CalcPos> calcList = new ArrayList<CalcPos>();
		for (Pos pos : list) {
			int num = bord.DoSet(pos,getMyColor(),false);
			calcList.add(new CalcPos(pos,num));
		}
		return calcList;
	}



	class CalcPos{

		private Pos pos;
		private int num;

		public CalcPos(Pos pos,int num){
			this.pos = pos;
			this.num = num;
		}

		public Pos getPos() {
			return pos;
		}
		public void setPos(Pos pos) {
			this.pos = pos;
		}
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
	}

}


