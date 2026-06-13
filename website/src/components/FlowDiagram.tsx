import styles from "./FlowDiagram.module.scss";

type FlowDiagramProps = {
  className?: string;
  steps: string[];
};

export function FlowDiagram({ className, steps }: FlowDiagramProps) {
  return (
    <ol className={`${styles.flow} ${className ?? ""}`}>
      {steps.map((step) => (
        <li key={step}>
          <span>{step}</span>
        </li>
      ))}
    </ol>
  );
}
