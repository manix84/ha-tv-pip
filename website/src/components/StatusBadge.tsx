import styles from "./StatusBadge.module.scss";

export type StatusKind = "complete" | "planned" | "future";

const labels: Record<StatusKind, string> = {
  complete: "Complete",
  planned: "Planned",
  future: "Future"
};

export function StatusBadge({ status }: { status: StatusKind }) {
  return <span className={`${styles.badge} ${styles[status]}`}>{labels[status]}</span>;
}
