package game.SpellBend.organize;

public class RankObj {
  public final String rankName;
  public final int ranking;
  public final String CCedRankName;
  public final String playerNameCC;
  public final String bracketsCC;
  
  public RankObj (String rankName, int ranking, String CCedRankName, String playerNameCC, String bracketsCC) {
    this.rankName = rankName;
    this.ranking = ranking;
    this.CCedRankName = CCedRankName;
    this.playerNameCC = playerNameCC;
    this.bracketsCC = bracketsCC;
  }
  
  public RankObj (String rankName, int ranking, String CCedRankName, String restCC) {
    this.rankName = rankName;
    this.ranking = ranking;
    this.CCedRankName = CCedRankName;
    this.playerNameCC = restCC;
    this.bracketsCC = restCC;
  }
}