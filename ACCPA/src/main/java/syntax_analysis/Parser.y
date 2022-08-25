%language "Java"
%define api.package {syntax_analysis}
%define api.prefix {Parser}
%define api.parser.class {Parser}
%define api.parser.public

%code imports {
	import lexical_analysis.tokens.EvalToken;
        import lexical_analysis.tokens.IdentifierToken;
        import lexical_analysis.tokens.arithmetic_function.DivideToken;
        import lexical_analysis.tokens.arithmetic_function.MinusToken;
        import lexical_analysis.tokens.arithmetic_function.PlusToken;
        import lexical_analysis.tokens.arithmetic_function.TimesToken;
        import lexical_analysis.tokens.comparison.*;
        import lexical_analysis.tokens.literal.BooleanLiteralToken;
        import lexical_analysis.tokens.literal.IntegerNumberLiteralToken;
        import lexical_analysis.tokens.literal.RealNumberLiteralToken;
        import lexical_analysis.tokens.literal.StringLiteralToken;
        import lexical_analysis.tokens.logical_operator.AndToken;
        import lexical_analysis.tokens.logical_operator.NotToken;
        import lexical_analysis.tokens.logical_operator.OrToken;
        import lexical_analysis.tokens.logical_operator.XorToken;
        import lexical_analysis.tokens.operation_on_lists.ConsToken;
        import lexical_analysis.tokens.operation_on_lists.HeadToken;
        import lexical_analysis.tokens.operation_on_lists.TailToken;
        import lexical_analysis.tokens.predicate.*;
        import syntax_analysis.node.*;
        import syntax_analysis.node.special_form.*;
        import syntax_analysis.node.type_node.*;

        import java.io.FileReader;
        import java.io.IOException;
}

%code {
	static AST ast;
        public static AST makeAST(String sourceProgramPath) throws IOException {
		FileReader fileReader = new FileReader(sourceProgramPath);
		LexerAdapter lexerAdapter = new LexerAdapter(fileReader);
		Parser p = new Parser(lexerAdapter);
		p.parse();
		return ast;
	}
}

%token <OpenParenthesisToken> OpenParenthesisToken
%token <CloseParenthesisToken> CloseParenthesisToken
%token <IntegerNumberLiteralToken> IntegerNumberLiteralToken
%token <RealNumberLiteralToken> RealNumberLiteralToken
%token <BooleanLiteralToken> BooleanLiteralToken
%token <IdentifierToken> IdentifierToken
%token <QuoteToken> QuoteToken
%token <QuoteShortToken> QuoteShortToken
%token <SetQToken> SetQToken
%token <FuncToken> FuncToken
%token <LambdaToken> LambdaToken
%token <ProgToken> ProgToken
%token <CondToken> CondToken
%token <PlusToken> PlusToken
%token <MinusToken> MinusToken
%token <TimesToken> TimesToken
%token <DivideToken> DivideToken
%token <HeadToken> HeadToken
%token <TailToken> TailToken
%token <ConsToken> ConsToken
%token <EqualToken> EqualToken
%token <NonEqualToken> NonEqualToken
%token <LessToken> LessToken
%token <LessEqToken> LessEqToken
%token <GreaterToken> GreaterToken
%token <GreaterEqToken> GreaterEqToken
%token <IsIntToken> IsIntToken
%token <IsRealToken> IsRealToken
%token <IsBoolToken> IsBoolToken
%token <IsAtomToken> IsAtomToken
%token <IsListToken> IsListToken
%token <AndToken> AndToken
%token <OrToken> OrToken
%token <XorToken> XorToken
%token <NotToken> NotToken
%token <EvalToken> EvalToken
%token <DefineToken> DefineToken
%token <FunctypeToken> FunctypeToken


%token <IntToken> IntToken
%token <DoubleToken> DoubleToken
%token <BooleanToken> BooleanToken
%token <NumToken> NumToken
%token <StringToken> StringToken
%token <AnyToken> AnyToken
%token <UnitToken> UnitToken
%token <AutoToken> AutoToken
%token <GetAtToken> GetAtToken
%token <LetToken> LetToken
%token <SetAtToken> SetAtToken
%token <StringLiteralToken> StringLiteralToken
%token <ArrowToken> ArrowToken
%token <CommaToken> CommaToken

%type Program
%type SpecialForm
%type <ElementInterface> Element
%type <ListNode> Elements
%type <ListNode> TupleElements
%type <AtomNode> Atom
%type <LiteralNode> Literal
%type <ElementInterface> List
%type <ElementInterface> SpecialForm
%type <NodeType> Type
%type <ListOfTypes> Types
%type <ListOfTypes> TupleTypes
%type <NodeType> BaseType
%type <FunctionType> FunctionType

%start Program

%%

Program
	: Element {ast = new AST(new ListNode($1));}
       	| Element Elements {ast = new AST(new ListNode($1, $2)); }
	;
Elements
	: /* empty */  {$$ = new ListNode();}
	| Element Elements {$$ = new ListNode($1, $2);}
	;
TupleElements
	: /* empty */  {$$ = new ListNode();}
	| CommaToken Element TupleElements {$$ = new ListNode($2, $3);}
	;
Element
	: Atom {$$ = $1;}
	| Literal {$$ = $1;}
	| List {$$ = $1;}
	| QuoteShortToken Element {$$ = new QuoteNode($2);}
	| OpenParenthesisToken Element CommaToken Element TupleElements CloseParenthesisToken {$$ = new TupleNode($2, $4, $5);}
	;
List
	: OpenParenthesisToken Element Elements CloseParenthesisToken {$$ = new ListNode($2, $3);}
	| OpenParenthesisToken SpecialForm CloseParenthesisToken {$$ = $2;}
	| OpenParenthesisToken CloseParenthesisToken {$$ = new ListNode();}
	;
SpecialForm
	: QuoteToken Element {$$ = new QuoteNode($2);}
	| DefineToken Atom Type {$$ = new DefineNode($2, $3);}
	| SetQToken Atom Element {$$ = new SetQNode($2, $3);}
	| FunctypeToken Atom FunctionType {$$ = new FunctypeNode($2, $3);}
	| FuncToken Atom List Element {$$ = new FuncNode($2, $3, $4);}
	| LambdaToken List Element {$$ = new LambdaNode($2, $3);}
	| ProgToken List Element {$$ = new ProgNode($2, $3);}
	| CondToken Element Element Element {$$ = new CondNode($2, $3, $4);}
	;
Type
	: BaseType {$$ = $1;}
	| FunctionType {$$ = $1;}
	;
Types
	: /* empty */ {$$ = new ListOfTypes();}
	| Type Types {$$ = new ListOfTypes($1, $2);}
	;
TupleTypes
	: /* empty */ {$$ = new ListOfTypes();}
	| CommaToken Type TupleTypes {$$ = new ListOfTypes($2, $3);}
	;
BaseType
	: IntToken {$$ = new IntType();}
	| DoubleToken {$$ = new DoubleType();}
	| BooleanToken {$$ = new BooleanType();}
	| StringToken {$$ = new StringType();}
	| NumToken {$$ = new NumType();}
	| AnyToken {$$ = new AnyType();}
	| UnitToken {$$ = new UnitType();}
	| AutoToken {$$ = new AutoType();}
	| OpenParenthesisToken Type CloseParenthesisToken {$$ = new ListType($2);}
	| OpenParenthesisToken Type CommaToken Type TupleTypes CloseParenthesisToken {$$ = new TupleType($2, $4, $5);}
	;
FunctionType
	: OpenParenthesisToken Type Types ArrowToken Type CloseParenthesisToken {$$ = new FunctionType($2, $3, $5);}
	;
Atom
	: IdentifierToken {$$ = new AtomNode($1);}
	| PlusToken {$$ = new AtomNode($1);}
	| MinusToken {$$ = new AtomNode($1);}
	| TimesToken {$$ = new AtomNode($1);}
	| DivideToken {$$ = new AtomNode($1);}
	| HeadToken {$$ = new AtomNode($1);}
	| TailToken {$$ = new AtomNode($1);}
	| ConsToken {$$ = new AtomNode($1);}
	| EqualToken {$$ = new AtomNode($1);}
	| NonEqualToken {$$ = new AtomNode($1);}
	| LessToken {$$ = new AtomNode($1);}
	| LessEqToken {$$ = new AtomNode($1);}
	| GreaterToken {$$ = new AtomNode($1);}
	| GreaterEqToken {$$ = new AtomNode($1);}
	| IsIntToken {$$ = new AtomNode($1);}
	| IsRealToken {$$ = new AtomNode($1);}
	| IsBoolToken {$$ = new AtomNode($1);}
	| IsAtomToken {$$ = new AtomNode($1);}
	| IsListToken {$$ = new AtomNode($1);}
	| AndToken {$$ = new AtomNode($1);}
	| OrToken {$$ = new AtomNode($1);}
	| XorToken {$$ = new AtomNode($1);}
	| NotToken {$$ = new AtomNode($1);}
	| EvalToken {$$ = new AtomNode($1);}
	;
Literal
	: IntegerNumberLiteralToken {$$ = new LiteralNode($1);}
	| RealNumberLiteralToken {$$ = new LiteralNode($1);}
	| BooleanLiteralToken {$$ = new LiteralNode($1);}
	| StringLiteralToken {$$ = new LiteralNode($1);}
	;
%%
