package com.beduka.utils;

/**
*
* @author Megatron
*
*/
public class Megatron extends AdvancedRobot{

   // Meus dados
   static final int firePower = 3;
   static final int fireSpeed = 20 - firePower * 3;
   static int missedShots = 0;
   static final int maxMatchLen = 30;
   static boolean changeDirection = false;
   static int wallDistance = 80;

   // Dados do inimigo
   static double enemyEnergy;
   static int distance = 36;
   static String enemyHistory = "";


   /**
    * Método de inicialização do robo.
    */
   public void run(){
       // Roda o radar até o infinito do planeta;
       turnRadarRightRadians(Double.POSITIVE_INFINITY);
   }

   /**
    * Evento chamado quando o radar encontra um inimigo.
    */
   public void onScannedRobot(ScannedRobotEvent e){
      
       // Posiciona o radar sobre o inimigo
       setTurnRadarLeft(getRadarTurnRemaining());
      
       // Retorna o ângulo que o inimigo está.
       double enemyAngle = e.getBearingRadians();
      
       // Retorna a distancia que o inimigo está.
       double enemyDistance = e.getDistance();
      
       int matchLen = maxMatchLen;
       int matchPos;
       int i;
      
       // Move o tanque no co-seno do ângulo do inimigo.
       setTurnRightRadians(Math.cos(enemyAngle));
      
       enemyAngle += getHeadingRadians();
      
       // Targeting - SingleTick PM
       enemyHistory = String.valueOf((char) (e.getVelocity() * Math.sin(e.getHeadingRadians() - enemyAngle))).concat(enemyHistory);
       while((matchPos = enemyHistory.indexOf(enemyHistory.substring(0, matchLen--), i = (int) (enemyDistance) / fireSpeed)) < 0);
       do {
           enemyAngle += (short) enemyHistory.charAt(--matchPos) / enemyDistance;
       } while (--i > 0);
      
       setTurnGunRightRadians(Utils.normalRelativeAngle(enemyAngle - getGunHeadingRadians()));
      
       // Se eu perdi 5 tiros, diminui a potência.
       setFire(missedShots > 5 ? 0.1 : 2);
      
       // Movement - Stop and Go
       if (enemyEnergy > (enemyEnergy = e.getEnergy())) {
           setAhead(distance);
       }
          
       // Chamada de método.
//       dontHitWall();
   }
  
   /**
    * Evento chamado quando o robo bate na parede.
    */
   public void onHitWall(HitWallEvent event) {
       // Se bater anda para a distancia contrária.
       distance = -distance;
   }
  
   /**
    * Evento chamado quando o tiro não acerta nenhum alvo.
    */
   public void onBulletMissed(BulletMissedEvent event) {
       // Quantidade de tiros perdidos.
       missedShots++;
   }
  
   /**
    * Evento chamado quando o tira acerta um alvo.
    */
   public void onBulletHit(BulletHitEvent event) {
       // Zera o contador.
       missedShots = 0;
   }
  
   /**
    * Método que faz o tanque não bater na parede.
    */
   private void dontHitWall() {
       if ((getX() < wallDistance || getY() < wallDistance ||
           getX() > getBattleFieldWidth() - wallDistance ||
           getY() > getBattleFieldHeight() - wallDistance) &&
           !changeDirection){
           out.print("L I M I T E !!!!");
           changeDirection = true;
          
       }
       if (changeDirection){
           distance = -distance;
           setAhead(distance);
       }
   }
}
