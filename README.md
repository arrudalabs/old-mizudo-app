# Mizu-do App

Controle de associados de uma associação de Karatê chamada Mizu-do

Funcionalidades pensadas:

 - Controle Membros
 
Estrutura:
  - Membro (Entidade)
    - nome completo
    
  - DadosGeraisMembro (Entidade)
    - membro
    - data nascimento
    - Endereço
      - logradouro
      - numero
      - complemento
      - Cidade
      - UF
      - CEP
    
  - DadosFisicosMembro (Entidade)
    - sexo
    - peso
    - altura
    
  - ExameMedicoMembro (Entidade)
    - membro
    - data
    - obs

  - ResposavelMembro (Entidade)
    - membro
    - nome
    - Endereço
        - logradouro
        - numero
        - complemento
        - Cidade
        - UF
        - CEP
  
  - EmailMembro (Entidade)
    - membro
    - email

  - TelefoneMembro (Entidade)
    - membro
    - numero
    - contato

  - GraduacaoMembro (Entidade)
    - membro
    - graduacao - KYUs e DANs
        - 7 kyu até 1 kyu / 1 dan...
    - data
    
  - PapelMembro (Entidade)
    - membro
    - papel
      - ALUNO
      - INSTRUTOR
      - EXAMINADOR
      - COORDENADOR
     
  - Turmas
    - horario
    - descrição da turma

  - Faltas
    - membro
    - turma
    - data

  - AvaliacaoMembro (Entidade)
    - membro
    - data
    - examinador
    - conferente
    - graduação - KYUs e DANs
      - 7 kyu até 1 kyu / 1 dan...
    - Kihon -> Sequências de movimentos pré estabelecidos em 5 passos
        - Notas -> 0.0 até 10.0
    - Kata -> Sequências de movimentos base de cada estilo de Karatê
        - Notas -> 0.0 até 10.0
    - Kumite -> Aplicação em forma de sparring ou combate livre
        - Notas -> 0.0 até 10.0

Regra negócio: o aluno para ser aprovado, precisa ter uma média acima de 5 em todas as categorias;

Relação de atletas por peso/altura/idade/graduação
  
  