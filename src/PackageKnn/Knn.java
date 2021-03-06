
/*Algoritmo KNN 
 Esse algoritimo determina a qual classe determinado elemento pertence baseado 
 nos seus vizinhos mais proximos.
 A classe que tiver maior quantidade de representantes com vizinhos do elemento de teste define o elemento. 
 Cada elemento possui uma coordenadas(caracteristicas) de um campo mutildimencional e seu tipo.
 exemplo " 5.1,3.5,1.4,0.2,Iris-setosa ".
 Esse algoritimo ler um arquivo contendo varios elementos, separa esse elementos em grupos de teste e de treinamento
 onde os elemntos do grupo de teste serão os elemntos ao qual desejamos classificar e os de treimentos serão os vizinhos.
 e ao final compara o resultado obitdo atraves do algorimo com a resposta e calcula a porcentagem de acerto.

 A variavel K define quantos vizinhos serão considerados.
 uma conclusão chegada com esse trabalho é que quanto maior o for K maior é o
 nivel de ruido nos dados logo a media da porcentagem de acerto cai.
*
*/


package PackageKnn;
import java.io.*;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Math;


public class Knn {

		static ArrayList< Dado > arrayListTodosDados= new ArrayList< Dado >();
		static ArrayList< Dado > arrayListTreinamento = new ArrayList< Dado >();
		static ArrayList< Dado > arraListTeste= new ArrayList< Dado >();
		static int QuantidadeDeatributos = 4;
		static int k = 12;//Define a quantidade de visinhos que será considerados para a verificação
		
		//Objeto de dados 
		static class Dado{
				//Nome da classe
				String classe;
				// Vetor de coordenadas
				double atributos[] = new double[QuantidadeDeatributos];
		
				public String getClasse() {
						return classe;
				}

				public void setClasse(String classe) {
						this.classe = classe;
				}
	
		}	
		
		
		//Objeto utilizado para blocos de distancia e contagem de classe
		static class VizinhosDistancia {
	
				String classe;
				double distancia;
			
				public String getClasse() {
						return classe;
				}

				public void setClasse(String classe) {
						this.classe = classe;
				}

				public double getDistancia() {
						return distancia;
				}

				public void setDistancia(double distancia) {
						this.distancia = distancia;
				}
		
		}
		
		
		//Preenche um array do tipo dado utilizando aquivos
		static void separarDadosTesteETreinamento(ArrayList<Dado>arrayListTreinamento, ArrayList<Dado>arrayListTeste, ArrayList<Dado>arrayListdados){
					
				//Define o tamanho do arrayListTreinamento neste caso 67% do tamanho arrayListDados
				int treinamento = (67 * arrayListdados.size() ) / 100;
					
				
					
				//Retira dados do arrey de dados para arrey de treinamento
				while(treinamento != 0 ){
						Random random = new Random();
						
						//Sorteia um index com valor dentro do tamanho atual do array
						int x = random.nextInt(arrayListdados.size());
						
						//Copia o objeto do array dado para arrayTreinamento
						arrayListTreinamento.add(arrayListdados.get(x));
					
						//Remove do arrayListDados e evitar repetição
						arrayListdados.remove(x);
						
						treinamento--;
				}
					
					
				//Selecionar dados para para teste
				//Nesta fase arralistDado e 33% do tamanho original
				while( arrayListdados.size() > 0 ){
						
						Random random = new Random();
						//Sorteia um index com valor dentro do tamanho atual do array
						int x = random.nextInt(arrayListdados.size());
						
						
						//Copia o objeto do array dado para arrayListeTeste
						arrayListTeste.add(arrayListdados.get(x));
					
						//Remove objeto do arrayListDados para evitar repetição
						arrayListdados.remove(x);
						
				}
					
				
					
		}//Fim  separarDadosTesteEDesconecidos
		
		
		
		//Preencher um array do tipo dado utilizando arquivo
		static void PreencherArrayDeDados( ArrayList<Dado>arrayListDados){
				
				try {
						FileReader arq = new FileReader("/home/davidg/Documentos/ProgramasJava/ProgramasComEclip/AlgoritimoKnn/src/PackageKnn/iris.csv");
						BufferedReader lerArq = new BufferedReader(arq);
						String linha = lerArq.readLine(); 
						//int i = 0;
						
						while (linha != null) {
			        
								String[] vetorDeString = null;
								
								//Remove da string parte nula da linha
								String[] removeLinhaBranca; 
								removeLinhaBranca = linha.split(" ");
								//Divide string 
								vetorDeString = removeLinhaBranca[0].split(",");	
								
								Dado aux = new Dado();
								
								aux.atributos[0]=Double.parseDouble(vetorDeString[0]);
								aux.atributos[1]=Double.parseDouble(vetorDeString[1]);
								aux.atributos[2]=Double.parseDouble(vetorDeString[2]);
								aux.atributos[3]=Double.parseDouble(vetorDeString[3]);
								aux.classe = vetorDeString[4];
								arrayListDados.add(aux);
			        
						
					
						
			       
								linha = lerArq.readLine(); // lê da segunda até a última linha
						}

						arq.close();
			       }catch (IOException e) {
			    	   		System.err.printf("Erro na abertura do arquivo: %s.\n",
			    	   		e.getMessage());
			       }

			    
			  }//Fim PreencherArrayDeDados
		
		
		
		/*Recebe dois array do tipo Dado, um para teste e outro treinamento em seguida calcula distancia, ordena pela distacia, seleciona k  primeiros,
		faz contagem por classe , defiene a classe do idividuo , verifica a resposta e por fim calcula a porcentagem de acertos  
		*/
		public static void classificar(ArrayList<Dado>arrayListTeste, ArrayList<Dado>arraylistTreinamento){
		
		//Variavel utilizada para contar a porcentagem de acerto
		int contadorAcerto = 0;
		
		//Percorre cada elemento do arrayListTeste e calculando a distancia entre cada elemento do arraylistTreinamento 
		for(int z=0 ; z < arrayListTeste.size(); z++){
				ArrayList< VizinhosDistancia > vizinhosDistancia = new ArrayList< VizinhosDistancia >();
			
				//Percorre arraylistTreinamento e para cada elemento calcular a distancia entre o elemento atual do arrayListTeste e salva no visinhosDistancia
				for(int i=0;i < arraylistTreinamento.size();i++){
					VizinhosDistancia aux = new VizinhosDistancia();
						aux.classe = arraylistTreinamento.get(i).classe;
						
						//Distancia euclidiana
						aux.distancia =  Math.sqrt(Math.pow( (arraylistTreinamento.get(i).atributos[0] - arrayListTeste.get(z).atributos[0]),2 ) + Math. pow((arraylistTreinamento.get(i).atributos[1] - arrayListTeste.get(z).atributos[1]),2) + Math.pow( (arraylistTreinamento.get(i).atributos[2] - arrayListTeste.get(z).atributos[2]),2 )+ Math.pow( (arraylistTreinamento.get(i).atributos[3] - arrayListTeste.get(z).atributos[3]),2 ) ) ;
					
						vizinhosDistancia.add(aux);
				}
		
				
		 //Coloca em ordem crescente pelo metodo bolha 
		 for(int x = 0; x < vizinhosDistancia.size(); x++ ){
			    for(int  y = x + 1; y < vizinhosDistancia.size(); y++ ) // sempre 1 elemento à frente
			    {
			    		// Se o (x > (x+1)) então o x passa pra frente (ordem crescente)
			    		if (vizinhosDistancia.get(x).getDistancia()  >  vizinhosDistancia.get(y).getDistancia() )
			    		{
			   
			    			VizinhosDistancia aux = new VizinhosDistancia();
			    				aux = vizinhosDistancia.get(x);	
			    				vizinhosDistancia.set(x,vizinhosDistancia.get(y));
			    				vizinhosDistancia.set(y,aux);
			    		}
			    }
		}//Fim  ordenação
			
		
		 
		 //Colocar os k primeiros vizinhos em uma nova array
		 ArrayList< VizinhosDistancia > kprimeirosVisinhos  = new ArrayList< VizinhosDistancia >();
		 
		 for(int i=0;i<k;i++){
				kprimeirosVisinhos.add(vizinhosDistancia.get(i));
		 }
			
		 
		 
		 //Indenetificar e contar os elementos de cada classe que estao entre os k elementos
		 //Aqui foi tomado emprestado um array do tipo VisinhoDistancia para faze a contagem por classe
		 ArrayList< VizinhosDistancia > ArrayListContagemPorClasse  = new ArrayList< VizinhosDistancia >();
			
		 //Percorre kprimeirosVisinhos
		 for(int i=0; i<kprimeirosVisinhos.size(); i++){
			 	boolean  acho = false;
			 	
			 	//Percorre todo ArrayListContagemPorClasse
			 	for(int j=0; j<ArrayListContagemPorClasse.size(); j++){
			 			
			 			// Si classe atual do kprimeirosVisinhos ja tiver representante no ArrayListContagemPorClasse entao so somar
						if(kprimeirosVisinhos.get(i).getClasse().equals(ArrayListContagemPorClasse.get(j).getClasse())){
								ArrayListContagemPorClasse.get(j).setDistancia(ArrayListContagemPorClasse.get(j).getDistancia()+1);
								//Achou representante achou recebe verdeiro
								acho = true;
						}
			 	}
				//Caso a classe atual nao tenha representante inserir o primeiro representante da classe
			 	if(acho== false){
			 		VizinhosDistancia aux = new VizinhosDistancia();
						aux.setClasse(kprimeirosVisinhos.get(i).getClasse());
						aux.setDistancia(1);
						ArrayListContagemPorClasse.add(aux);
				}
			
			
		}
			
		
		//Definir classe do idividuo de teste(desconhecido) atraves da classe com maior numero de integrantes
		int maior=0;
		for(int j=0; j<ArrayListContagemPorClasse.size()-1; j++){
				if(		ArrayListContagemPorClasse.get(j).getDistancia() < ArrayListContagemPorClasse.get(j+1).getDistancia()){
						maior=j;
				}
		}
			
		//Imprimir comparaçoes
		System.out.println("CLASSE ESPERADA:  "+arrayListTeste.get(z).getClasse()+"     CLASSE ENCONTRADA:  "+ ArrayListContagemPorClasse.get(maior).getClasse());
			
		//Conta acertos
		if(arrayListTeste.get(z).getClasse().equals(ArrayListContagemPorClasse.get(maior).getClasse())){
				contadorAcerto++;
		}	
	
		}// FIM //Percorre cada elemento do arrayListTeste e calculando a distancia entre cada elemento do arraylistTreinamento 
		
		//Imprime a porcentagem de acerto
		System.out.println( "\n PORCENTAGEM DE ACERTO: "+(contadorAcerto * 100 ) / arrayListTeste.size()+"%" ) ;
	
	}//Fim classificar
		
		
		public static void main(String[] args){
				PreencherArrayDeDados(arrayListTodosDados);
				separarDadosTesteETreinamento( arrayListTreinamento, arraListTeste, arrayListTodosDados);			
				classificar(arraListTeste,  arrayListTreinamento);
				int [][]matriz= new int[6][6];
				

				
			
		}	

}
