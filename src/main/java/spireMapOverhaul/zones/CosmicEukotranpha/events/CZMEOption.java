package spireMapOverhaul.zones.CosmicEukotranpha.events;import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
public abstract class CZMEOption{public String title="";public String des="";public CZMEOption[]options=null;public String[]optionsStrings=null;
public boolean disabled=false;public AbstractCard previewCard=null;public AbstractRelic previewRelic=null;public String nextPart="";
public int nextScreenNum=0;public int nextScreenFunc=0;public int nextActiveNum=0;public String nextScn="";public String nextimgurl="";public boolean willFinish=false;
public String identifier="";
public String ID="I probably need this even though I don't use this ever";
public CZMEOption(String title){this.title=title;}
public CZMEOption(String title,boolean disabled){this.title=title;this.disabled=disabled;}

/*
public CZMEOption(String title,String des,CZMEOption[]options,boolean disabled,AbstractCard previewCard,AbstractRelic previewRelic,
																		String nextPart,int nextScreenNum,int nextScreenFunc,int nextActiveNum,String nextScn,String nextimgurl,boolean willFinish){
	this.title=title;this.des=des;this.options=options;this.disabled=disabled;this.previewCard=previewCard;this.previewRelic=previewRelic;
	this.nextPart=nextPart;this.nextScreenNum=nextScreenNum;this.nextScreenFunc=nextScreenFunc;this.nextActiveNum=nextActiveNum;this.nextScn=nextScn;this.nextimgurl=nextimgurl;this.willFinish=willFinish;}
*/
public CZMEOption(String identifier,String title,String des,String[]options,boolean disabled,AbstractCard previewCard,AbstractRelic previewRelic,
																		String nextPart,int nextScreenNum,int nextScreenFunc,int nextActiveNum,String nextScn,String nextimgurl,boolean willFinish){
	this.identifier=identifier;this.title=title;this.des=des;this.optionsStrings=options;this.disabled=disabled;this.previewCard=previewCard;this.previewRelic=previewRelic;
	this.nextPart=nextPart;this.nextScreenNum=nextScreenNum;this.nextScreenFunc=nextScreenFunc;this.nextActiveNum=nextActiveNum;this.nextScn=nextScn;this.nextimgurl=nextimgurl;this.willFinish=willFinish;}



}
